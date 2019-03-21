package com.faire.orders.faireorders.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class ProductOption {
    private String id;
    private String productId;
    private boolean active;
    private String name;
    private String sku;
    private int availableQuantity;
    private String backorderedUntil;
    private String createdAt;
    private String updatedAt;
}
