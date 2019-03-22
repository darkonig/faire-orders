package com.faire.orders.faireorders.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnalyzesResult {
    private ProductOptionPrice bestSellingProduct;
    private OrderPrice largestOrder;
    private StateOrder stateMostOrders;
    private StateOrder stateGreatestOrders;
    private OrderPrice orderWithGreatestTests;
}
