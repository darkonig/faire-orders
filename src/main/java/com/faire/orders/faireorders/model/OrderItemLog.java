package com.faire.orders.faireorders.model;

import com.faire.orders.faireorders.domain.OrderItem;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderItemLog {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column
    @JsonProperty("oi_id")
    private String oiId;
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

    private boolean processed;
    private boolean backordered;

    @JsonProperty("updated_at")
    @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime updatedAt;

    public static OrderItemLog from(OrderItem item) {
        OrderItemLog t = new OrderItemLog();
        t.oiId = item.getId();
        t.orderId = item.getOrderId();
        t.productId = item.getProductId();
        t.productOptionId = item.getProductOptionId();
        t.quantity = item.getQuantity();
        t.sku = item.getSku();
        t.priceCents = item.getPriceCents();
        t.productName = item.getProductName();
        t.productOptionName = item.getProductOptionName();
        t.includesTester = item.isIncludesTester();
        t.testerPriceCents = item.getTesterPriceCents();

        t.processed = item.isProcessed();
        t.backordered = !t.processed;
        return t;
    }
}
