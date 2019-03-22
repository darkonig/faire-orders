package com.faire.orders.faireorders.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductOption {
    private String id;
    @JsonProperty("product_id")
    private String productId;
    private boolean active;
    private String name;
    private String sku;
    @JsonProperty("available_quantity")
    private int availableQuantity;
    @JsonProperty("backordered_until")
    private String backorderedUntil;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
}
