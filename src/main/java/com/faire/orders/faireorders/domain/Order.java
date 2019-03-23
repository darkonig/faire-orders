package com.faire.orders.faireorders.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Order {
    private String id;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    private OrderState state;
    private List<OrderItem> items;
    private List<Shipment> shipments;
    private Address address;

    public long getTotalUnits() {
        if (items == null) {
            return 0;
        }
        return items.stream().filter(e -> e.isProcessed()).mapToLong(OrderItem::getQuantity).sum();
    }

    public long getTotalPriceCents() {
        if (items == null) {
            return 0;
        }
        return items.stream().filter(e -> e.isProcessed()).mapToLong(OrderItem::getTotalPriceCents).sum();
    }

    public long getTotalTesterPriceCents() {
        if (items == null) {
            return 0;
        }
        return items.stream().filter(e -> e.isProcessed()).mapToLong(OrderItem::getTotalTesterPriceCents).sum();
    }
}
