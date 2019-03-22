package com.faire.orders.faireorders.processor;

import com.faire.orders.faireorders.domain.BackorderItem;
import com.faire.orders.faireorders.domain.ProductOptionUpdateRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderProcessorResult {
    private ProductOptionUpdateRequest productOptionUpdateRequest;
    private BackorderItem backorderItem;
}
