package com.faire.orders.faireorders.service.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StateOrder {
    private String state;
    private long value;
}
