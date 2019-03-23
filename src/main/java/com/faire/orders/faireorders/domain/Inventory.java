package com.faire.orders.faireorders.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Inventory {
    private String sku;
    @JsonProperty("current_quantity")
    private int currentQuantity;
    private boolean discontinued;
    @JsonProperty("backordered_until")
    private String backorderedUntil;

    @JsonIgnore
    private int soldAmount;
}
