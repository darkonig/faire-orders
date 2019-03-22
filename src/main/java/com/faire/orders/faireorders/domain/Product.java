package com.faire.orders.faireorders.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
public class Product {
    private String id;
    @JsonProperty("brand_id")
    private String brandId;
    @JsonProperty("short_description")
    private String shortDescription;
    private String description;
    @JsonProperty("wholesale_price_cents")
    private int wholesalePriceCents;
    @JsonProperty("retail_price_cents")
    private int retailPriceCents;
    private boolean active;
    private String name;
    @JsonProperty("unit_multiplier")
    private int unitMultiplier;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    private List<ProductOption> options;

}
