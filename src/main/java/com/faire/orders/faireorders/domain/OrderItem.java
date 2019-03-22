package com.faire.orders.faireorders.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItem {
    private String id;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("product_id")
    private String productId;
    @JsonProperty("product_option_id")
    private String productOptionId;
    private int quantity;
    private String sku;
    @JsonProperty("price_cents")
    private int priceCents;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("product_option_name")
    private String productOptionName;
    @JsonProperty("includes_tester")
    private boolean includesTester;
    @JsonProperty("tester_price_cents")
    private int testerPriceCents;

    /*
     * Informs if this order item had been marked to process
     */
    @JsonIgnore
    private boolean processed;

    public long getTotalPriceCents() {
        return quantity * priceCents;
    }

    public long getTotalTesterPriceCents() {
        if (!includesTester || testerPriceCents <= 0)
            return 0;
        return quantity * testerPriceCents;
    }
}
