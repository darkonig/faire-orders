package com.faire.orders.faireorders.service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnalyzesResult {
    @JsonProperty("best_selling_product")
    private ProductOptionPrice bestSellingProduct;
    @JsonProperty("largest_order")
    private OrderPrice largestOrder;
    @JsonProperty("state_most_orders")
    private StateOrder stateMostOrders;
    @JsonProperty("state_greatest_orders")
    private StateOrder stateGreatestOrders;
    @JsonProperty("order_with_greatest_tests")
    private OrderPrice orderWithGreatestTests;
}
