package com.faire.orders.faireorders.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

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
    private long totalValue;
    @JsonIgnore
    private long testerTotalValue;

    public void addAvailableUnits(long availableUnits) {
        this.availableUnits += availableUnits;
    }

    public void addSoldUnits(long quantity) {
        this.soldUnits += quantity;
    }

    public void addTotalValue(long totalValue) {
        this.totalValue += totalValue;
    }

    public void addTesterTotalValue(long value) {
        this.testerTotalValue += value;
    }
}
