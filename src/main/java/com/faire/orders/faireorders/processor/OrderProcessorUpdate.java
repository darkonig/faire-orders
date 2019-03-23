package com.faire.orders.faireorders.processor;

import com.faire.orders.faireorders.domain.OrderItem;
import com.faire.orders.faireorders.domain.ProductOption;
import com.faire.orders.faireorders.domain.ProductOptionUpdateRequest;

public class OrderProcessorUpdate implements OrderProcessor {

    @Override
    public OrderProcessorResult process(OrderItem item, ProductOption option) {
        ProductOptionUpdateRequest opt = ProductOptionUpdateRequest.builder()
            .availableUnits(option.getAvailableQuantity() - item.getQuantity())
            .productOption(option)
            .soldUnits(item.getQuantity())
            .totalPrice(item.getTesterPriceCents())
            .testerTotalPrice(item.getTotalTesterPriceCents())
            .build();

        return OrderProcessorResult.builder()
            .productOptionUpdateRequest(opt)
            .build();
    }

}
