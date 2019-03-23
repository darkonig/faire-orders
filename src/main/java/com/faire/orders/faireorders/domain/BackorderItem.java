package com.faire.orders.faireorders.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BackorderItem {
    @JsonProperty("available_quantity")
    private int availableQuantity;

    private boolean discontinued;

    @JsonProperty("backordered_until")
    private String backorderedUntil;

    @JsonIgnore
    private OrderItem orderItem;
}
