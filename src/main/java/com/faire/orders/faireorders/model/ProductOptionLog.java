package com.faire.orders.faireorders.model;

import com.faire.orders.faireorders.domain.ProductOption;
import com.faire.orders.faireorders.domain.ProductOptionUpdateRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProductOptionLog {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @JsonProperty("option_id")
    private String optionId;
    @JsonProperty("product_id")
    private String productId;
    private boolean active;
    private String name;
    private String sku;
    @JsonProperty("available_quantity")
    private int availableQuantity;

    @JsonProperty("updated_at")
    @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime updatedAt;

    @JsonProperty("sold_units")
    private long soldUnits;
    @JsonProperty("total_price")
    private long totalPrice;
    @JsonProperty("tester_total_price")
    private long testerTotalPrice;

    public static ProductOptionLog from(ProductOptionUpdateRequest updateRequest) {
        ProductOptionLog t = new ProductOptionLog();

        ProductOption opt = updateRequest.getProductOption();
        t.optionId = opt.getId();
        t.productId = opt.getProductId();
        t.active = opt.isActive();
        t.name = opt.getName();
        t.sku = opt.getSku();
        t.availableQuantity = opt.getAvailableQuantity();
        t.soldUnits = updateRequest.getSoldUnits();
        t.totalPrice = updateRequest.getTotalPrice();
        t.testerTotalPrice = updateRequest.getTesterTotalPrice();

        return t;
    }
}
