package com.faire.orders.faireorders.service.impl;

import com.faire.orders.faireorders.domain.*;
import com.faire.orders.faireorders.domain.collections.EntityList;
import com.faire.orders.faireorders.entity.*;
import com.faire.orders.faireorders.exception.TechnicalException;
import com.faire.orders.faireorders.processor.OrderProcessor;
import com.faire.orders.faireorders.processor.OrderProcessorFactory;
import com.faire.orders.faireorders.processor.OrderProcessorResult;
import com.faire.orders.faireorders.service.FaireService;
import com.faire.orders.faireorders.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final FaireService service;

    public OrderServiceImpl(FaireService service) {
        this.service = service;
    }

    @Override
    public List<Product> getProducts(String accessToken, String brandId) {
        return getAll((page) -> service.getProducts(accessToken, page)).stream()
                .filter(e -> e.getBrandId().equals(brandId))
            .collect(Collectors.toList());
    }

    @Override
    public List<Order> getNewOrders(String accessToken) {
        //List<OrderState> exclusion = Stream.of(OrderState.values()).filter(e -> e != OrderState.NEW).collect(Collectors.toList());
        List<OrderState> exclusion = Collections.emptyList();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss.000'Z'");
        String shipAfter = dateTimeFormatter.format(ZonedDateTime.now(ZoneOffset.UTC));

        return getAll((page) -> service.getOrders(accessToken, page, exclusion, shipAfter)).stream()
                .filter(e -> e.getState() == OrderState.NEW)
            .collect(Collectors.toList());
    }

    @Override
    public ProcessOrderResult processOrder(String accessToken, List<Order> orders, List<Product> brandProductList) {
        logger.debug("[ProcessOrder] Orders: {}, Products: {}", orders, brandProductList);
        boolean isAcceptable = orders.stream().anyMatch(e -> OrderState.NEW != e.getState());
        if (isAcceptable) {
            logger.info("[ProcessOrder] Invalid order to process, the order must be NEW to be processed.");
            throw new TechnicalException("Invalid order to process, the order must be NEW to be processed.");
        }

        Map<String, ProductOption> options = brandProductList.stream()
                .flatMap(e -> e.getOptions().stream())
                .collect(Collectors.toMap(ProductOption::getId, e -> e));

        Map<String, Map<String, BackorderItem>> backorder = new HashMap<>();
        //InventoryResponse inventories = new InventoryResponse();
        Map<String, ProductOptionUpdateRequest> productOptionUpdate = new HashMap<>();
        List<OrderItem> orderItems = new ArrayList<>();
        List<Order> processOrders = new ArrayList<>();

        for (Order order: orders) {
            boolean containsOrder = false;

            /*for (OrderItem e : order.getItems()) {
                ProductOption option = options.get(e.getProductOptionId());
                if (option == null) {
                    continue;
                }
                if (option.getAvailableQuantity() < e.getQuantity()) {
                    Map<String, BackorderItem> item = backorder.get(order.getId());
                    if (item == null) {
                        item = new HashMap<>();
                        backorder.put(order.getId(), item);
                    }

                    item.put(e.getId(), BackorderItem.builder()
                        .availableQuantity(option.getAvailableQuantity())
                        .discontinued(!option.isActive())
                        .backorderedUntil(option.getBackorderedUntil())
                        .build());
                    continue;
                }

                ProductOptionUpdateRequest opt = productOptionUpdate.get(option.getId());
                if (opt == null) {
                    opt = ProductOptionUpdateRequest.builder()
                        .availableUnits(option.getAvailableQuantity() - e.getQuantity())
                        .productOption(option)
                        .build();
                    productOptionUpdate.put(option.getId(), opt);
                } else {
                    opt.addAvailableUnits(opt.getAvailableUnits() - e.getQuantity());
                }
                opt.addSoldUnits(e.getQuantity());
                opt.addTotalValue(e.getTotalPriceCents());
                opt.addTesterTotalValue(e.getTotalTesterPriceCents());

                option.setAvailableQuantity(opt.getAvailableUnits());
                e.setProcessed(true);

//                inventories.add(Inventory.builder()
//                        .sku(option.getSku())
//                        .backorderedUntil(option.getBackorderedUntil())
//                        .currentQuantity(option.getAvailableQuantity() - e.getQuantity())
//                        .discontinued(!option.isActive())
//                        .soldAmount(e.getQuantity())
//                        .build());
                orderItems.add(e);
                containsOrder = true;
            }*/

            for (OrderItem e : order.getItems()) {
                ProductOption option = options.get(e.getProductOptionId());
                if (option == null) {
                    continue;
                }
                OrderProcessor processor = OrderProcessorFactory.get(e, option);
                OrderProcessorResult result = processor.process(e, option);
                if (result.getBackorderItem() != null) {
                    Map<String, BackorderItem> item = backorder.get(order.getId());
                    if (item == null) {
                        item = new HashMap<>();
                        backorder.put(order.getId(), item);
                    }

                    item.put(e.getId(), result.getBackorderItem());
                } else if (result.getProductOptionUpdateRequest() != null) {
                    ProductOptionUpdateRequest opt = productOptionUpdate.get(option.getId());
                    if (opt == null) {
                        opt = result.getProductOptionUpdateRequest();
                        productOptionUpdate.put(option.getId(), opt);
                    } else {
                        ProductOptionUpdateRequest updateRequest = result.getProductOptionUpdateRequest();

                        opt.addSoldUnits(updateRequest.getSoldUnits());
                        opt.addTotalValue(updateRequest.getTotalValue());
                        opt.addTesterTotalValue(updateRequest.getTesterTotalValue());
                    }

                    option.setAvailableQuantity(opt.getAvailableUnits());
                    e.setProcessed(true);

                    orderItems.add(e);
                    containsOrder = true;
                }
            }

            if (containsOrder) {
                processOrders.add(order);
            }
        }

        logger.debug("[ProcessOrder] Update ProductOptionUpdate: {}, Order: {}, Backorder: {}", productOptionUpdate, processOrders, backorder);

        for (Map.Entry<String, ProductOptionUpdateRequest> entry : productOptionUpdate.entrySet()) {
            updateProductOption(accessToken, entry.getKey(), entry.getValue());
        }
        for (Order order : processOrders) {
            acceptOrder(accessToken, order);
        }
        for(Map.Entry<String, Map<String, BackorderItem>> entry : backorder.entrySet()) {
            backorderItems(accessToken, entry.getKey(), entry.getValue());
        }

        return ProcessOrderResult.builder()
                .backorders(backorder)
                .orders(processOrders)
                .productOptionUpdate(productOptionUpdate)
                .build();
    }

    @Override
    public AnalyzesResult getResultAnalytics(ProcessOrderResult result) {
        Map<String, ProductOptionUpdateRequest> backorders = result.getProductOptionUpdate();
        ProductOptionUpdateRequest bestSollingProduct = backorders.values().stream()
                .max(Comparator.comparing(ProductOptionUpdateRequest::getSoldUnits))
                .orElse(ProductOptionUpdateRequest.builder().build());

        List<Order> order = result.getOrders();
        Order maxOrder = order.stream().max(Comparator.comparing(Order::getTotalPriceCents)).orElse(Order.builder().build());

        Order maxOrderTesters = order.stream().max(Comparator.comparing(Order::getTotalTesterPriceCents)).orElse(Order.builder().build());

        Map<String, Long> statesPrices = order.stream()
                .collect(Collectors.groupingBy(e -> e.getAddress().getState(), Collectors.summingLong(Order::getTotalPriceCents)));
        StateOrder greatestStateValue = statesPrices.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> StateOrder.builder()
                        .state(e.getKey())
                        .value(e.getValue())
                        .build())
                .orElse(StateOrder.builder().build());

        Map<String, Long> statesUnits = order.stream()
                .collect(Collectors.groupingBy(e -> e.getAddress().getState(), Collectors.summingLong(Order::getTotalUnits)));
        StateOrder maxStateUnits = statesUnits.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> StateOrder.builder()
                        .state(e.getKey())
                        .value(e.getValue())
                        .build())
                .orElse(StateOrder.builder().build());

        return AnalyzesResult.builder()
                .bestSellingProduct(ProductOptionPrice.builder()
                        .option(bestSollingProduct.getProductOption())
                        .units(bestSollingProduct.getSoldUnits())
                        .build())
                .largestOrder(OrderPrice.builder()
                        .order(maxOrder)
                        .value(maxOrder.getTotalPriceCents())
                        .build())
                .orderWithGreatestTests(OrderPrice.builder()
                        .order(maxOrderTesters)
                        .value(maxOrderTesters.getTotalTesterPriceCents())
                        .build())
                .stateGreatestOrders(greatestStateValue)
                .stateMostOrders(maxStateUnits)
                .build();
    }

    boolean acceptOrder(String accessToken, Order order) {
        return service.acceptOrder(accessToken, order.getId());
    }

    Order backorderItems(String accessToken, String orderId, Map<String, BackorderItem> items) {
        return service.backorderItems(accessToken, orderId, items);
    }

    ProductOption updateProductOption(String accessToken, String optionId, ProductOptionUpdateRequest updateRequest) {
        return service.updateProductOption(accessToken, optionId, updateRequest);
    }

    private <T> List<T> getAll(Function<Integer, ? extends EntityList<T>> fn) {
        List<T> result = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        for (int page = 1; true; page++) {
            EntityList<T> list = fn.apply(page);
            if (list.getItems().size() > 2) {
                if (list.getItems().get(0) instanceof Order) {
                    ((Order)list.getItems().get(0)).setState(OrderState.NEW);
                }
                if (list.getItems().get(1) instanceof Order) {
                    ((Order)list.getItems().get(1)).setState(OrderState.NEW);
                }
            }

            try {
                logger.debug("Response data: {}", mapper.writeValueAsString(list));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            if (list == null || list.getItems() == null || list.getItems().isEmpty()) {
                break;
            }

            result.addAll(list.getItems());
        }



        return result;
    }
}
