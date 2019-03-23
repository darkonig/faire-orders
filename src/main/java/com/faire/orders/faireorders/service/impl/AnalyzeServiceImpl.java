package com.faire.orders.faireorders.service.impl;

import com.faire.orders.faireorders.domain.Order;
import com.faire.orders.faireorders.domain.ProductOptionUpdateRequest;
import com.faire.orders.faireorders.service.AnalyzeService;
import com.faire.orders.faireorders.service.entity.*;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyzeServiceImpl implements AnalyzeService {

    @Override
    public AnalyzesResult getResultAnalytics(ProcessOrderResult result) {
        Map<String, ProductOptionUpdateRequest> backorders = result.getProductOptionUpdate();
        ProductOptionUpdateRequest bestSoldProduct = backorders.values().stream()
                .max(Comparator.comparing(ProductOptionUpdateRequest::getSoldUnits))
                .orElse(ProductOptionUpdateRequest.builder().build());

        List<Order> order = result.getOrders();
        Order maxOrder = order.stream().max(Comparator.comparing(Order::getTotalPriceCents))
                .filter(e -> e.getTotalPriceCents() > 0)
                .orElse(Order.builder().build());

        Order maxOrderTesters = order.stream().max(Comparator.comparing(Order::getTotalTesterPriceCents))
                .filter(e -> e.getTotalTesterPriceCents() > 0)
                .orElse(Order.builder().build());

        Map<String, Long> statesPrices = order.stream()
                .collect(Collectors.groupingBy(e -> e.getAddress().getState(), Collectors.summingLong(Order::getTotalPriceCents)));
        StateOrder greatestStateValue = statesPrices.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> StateOrder.builder()
                        .state(e.getKey())
                        .value(e.getValue())
                        .build())
                .filter(e -> e.getValue() > 0)
                .orElse(StateOrder.builder().build());

        Map<String, Long> statesUnits = order.stream()
                .collect(Collectors.groupingBy(e -> e.getAddress().getState(), Collectors.summingLong(Order::getTotalUnits)));
        StateOrder maxStateUnits = statesUnits.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> StateOrder.builder()
                        .state(e.getKey())
                        .value(e.getValue())
                        .build())
                .filter(e -> e.getValue() > 0)
                .orElse(StateOrder.builder().build());

        return AnalyzesResult.builder()
                .bestSellingProduct(ProductOptionPrice.builder()
                        .option(bestSoldProduct.getProductOption())
                        .units(bestSoldProduct.getSoldUnits())
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

}
