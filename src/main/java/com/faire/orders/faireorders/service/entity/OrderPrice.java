package com.faire.orders.faireorders.service.entity;

import com.faire.orders.faireorders.domain.Order;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderPrice {
    private Order order;
    private long value;
}
