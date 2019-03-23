package com.faire.orders.faireorders.processor;

import com.faire.orders.faireorders.domain.BackorderItem;
import com.faire.orders.faireorders.domain.OrderItem;
import com.faire.orders.faireorders.domain.ProductOption;

public class OrderProcessorBackOrder implements OrderProcessor {

    @Override
    public OrderProcessorResult  process(OrderItem item, ProductOption option) {
        return OrderProcessorResult.builder()
            .backorderItem(BackorderItem.builder()
                .availableQuantity(option.getAvailableQuantity())
                .discontinued(!option.isActive())
                .backorderedUntil(option.getBackorderedUntil())
                .orderItem(item)
                .build())
            .build();
    }

}
