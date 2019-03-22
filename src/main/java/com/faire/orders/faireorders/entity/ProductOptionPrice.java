package com.faire.orders.faireorders.entity;

import com.faire.orders.faireorders.domain.ProductOption;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductOptionPrice {
    private ProductOption option;
    private long units;
}
