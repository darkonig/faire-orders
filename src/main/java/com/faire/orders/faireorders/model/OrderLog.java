package com.faire.orders.faireorders.model;

import com.faire.orders.faireorders.domain.Address;
import com.faire.orders.faireorders.domain.Order;
import com.faire.orders.faireorders.domain.OrderState;
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
public class OrderLog {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @JsonProperty("order_id")
    private String orderId;

    @Enumerated(EnumType.STRING)
    private OrderState status;

    private AddressLog address;

    @JsonProperty("updated_at")
    @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime updatedAt;

    public static OrderLog from(Order order) {
        OrderLog t = new OrderLog();
        t.orderId = order.getId();
        t.status = order.getState();
        t.address = AddressLog.from(order.getAddress());
        return t;
    }
}
