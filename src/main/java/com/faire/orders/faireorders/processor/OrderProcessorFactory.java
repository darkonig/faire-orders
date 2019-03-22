package com.faire.orders.faireorders.processor;

import com.faire.orders.faireorders.domain.OrderItem;
import com.faire.orders.faireorders.domain.ProductOption;

public class OrderProcessorFactory {

    public static OrderProcessor get(OrderItem item, ProductOption option) {
        if (option.getAvailableQuantity() < item.getQuantity()) {
            return new OrderProcessorBackOrder();
        }

        return new OrderProcessorUpdate();
    }

}
