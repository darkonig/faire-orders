package com.faire.orders.faireorders.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class Product {
    private String id;
    private String brandId;
    private String shortDescription;
    private String description;
    private int wholesalePriceCents;
    private int retailPriceCents;
    private boolean active;
    private String name;
    private int unitMultiplier;
    private String createdAt;
    private String updatedAt;

}
