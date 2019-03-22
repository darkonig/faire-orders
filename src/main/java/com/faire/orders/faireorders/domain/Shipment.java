package com.faire.orders.faireorders.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Shipment {
    private String id;
    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("maker_cost_cents")
    private int makerCostCents;
    private String carrier;
    @JsonProperty("tracking_code")
    private String trackingCode;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
}
