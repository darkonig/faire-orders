package com.faire.orders.faireorders.entity;

import com.faire.orders.faireorders.domain.BackorderItem;
import com.faire.orders.faireorders.domain.Order;
import com.faire.orders.faireorders.domain.ProductOptionUpdateRequest;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ProcessOrderResult {
    private Map<String, Map<String, BackorderItem>> backorders;
    private Map<String, ProductOptionUpdateRequest> productOptionUpdate;
    private List<Order> orders;
}
