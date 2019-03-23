package com.faire.orders.faireorders.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductOptionUpdateRequest {
    @JsonProperty ("available_units")
    private int availableUnits;

    @JsonIgnore
    private ProductOption productOption;

    @JsonIgnore
    private long soldUnits;
    @JsonIgnore
    private long totalPrice;
    @JsonIgnore
    private long testerTotalPrice;

    public void addAvailableUnits(long availableUnits) {
        this.availableUnits += availableUnits;
    }

    public void addSoldUnits(long quantity) {
        this.soldUnits += quantity;
    }

    public void addTotalValue(long totalValue) {
        this.totalPrice += totalValue;
    }

    public void addTesterTotalValue(long value) {
        this.testerTotalPrice += value;
    }
}
