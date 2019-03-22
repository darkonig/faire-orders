package com.faire.orders.faireorders.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StateOrder {
    private String state;
    private long value;
}
