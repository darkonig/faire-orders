package com.faire.orders.faireorders.service.impl;

import com.faire.orders.faireorders.domain.*;
import com.faire.orders.faireorders.domain.collections.EntityList;
import com.faire.orders.faireorders.exception.TechnicalException;
import com.faire.orders.faireorders.model.OrderItemLog;
import com.faire.orders.faireorders.model.OrderLog;
import com.faire.orders.faireorders.model.ProductOptionLog;
import com.faire.orders.faireorders.processor.OrderProcessor;
import com.faire.orders.faireorders.processor.OrderProcessorFactory;
import com.faire.orders.faireorders.processor.OrderProcessorResult;
import com.faire.orders.faireorders.service.EntityLogService;
import com.faire.orders.faireorders.service.FaireService;
import com.faire.orders.faireorders.service.OrderService;
import com.faire.orders.faireorders.service.entity.ProcessOrderResult;
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
    private final EntityLogService logService;

    public OrderServiceImpl(FaireService service, EntityLogService logService) {
        this.service = service;
        this.logService = logService;
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
        Map<String, ProductOptionUpdateRequest> productOptionUpdate = new HashMap<>();
        List<OrderItem> orderItems = new ArrayList<>();
        List<Order> processOrders = new ArrayList<>();

        for (Order order : orders) {
            boolean containsOrder = false;

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
                        opt.addTotalValue(updateRequest.getTotalPrice());
                        opt.addTesterTotalValue(updateRequest.getTesterTotalPrice());
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
        for (Map.Entry<String, Map<String, BackorderItem>> entry : backorder.entrySet()) {
            backorderItems(accessToken, entry.getKey(), entry.getValue());
        }

        return ProcessOrderResult.builder()
                .backorders(backorder)
                .orders(processOrders)
                .productOptionUpdate(productOptionUpdate)
                .build();
    }

    boolean acceptOrder(String accessToken, Order order) {
        logService.save(OrderLog.from(order));
        //return service.acceptOrder(accessToken, order.getId());
        return true;
    }

    Order backorderItems(String accessToken, String orderId, Map<String, BackorderItem> items) {
        List<OrderItemLog> save = items.entrySet().stream()
                .map(e -> OrderItemLog.from(e.getValue().getOrderItem()))
                .collect(Collectors.toList());
        logService.saveAll(save);
        //return service.backorderItems(accessToken, orderId, items);
        return null;
    }

    ProductOption updateProductOption(String accessToken, String optionId, ProductOptionUpdateRequest updateRequest) {
        logService.save(ProductOptionLog.from(updateRequest));
        //return service.updateProductOption(accessToken, optionId, updateRequest);
        return null;
    }

    private <T> List<T> getAll(Function<Integer, ? extends EntityList<T>> fn) {
        List<T> result = new ArrayList<>();

        for (int page = 1; true; page++) {
            EntityList<T> list = fn.apply(page);
            /*if (list.getItems().size() > 2) {
                if (list.getItems().get(0) instanceof Order) {
                    ((Order)list.getItems().get(0)).setState(OrderState.NEW);
                }
                if (list.getItems().get(1) instanceof Order) {
                    ((Order)list.getItems().get(1)).setState(OrderState.NEW);
                }
            }*/

            logger.debug("Response data: {}", list);


            if (list == null || list.getItems() == null || list.getItems().isEmpty()) {
                break;
            }

            result.addAll(list.getItems());
        }

        return result;
    }
}
