package com.faire.orders.faireorders.processor;

import com.faire.orders.faireorders.domain.OrderItem;
import com.faire.orders.faireorders.domain.ProductOption;

public interface OrderProcessor {

    OrderProcessorResult process(OrderItem item, ProductOption option);

}
