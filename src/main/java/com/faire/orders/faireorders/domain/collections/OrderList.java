package com.faire.orders.faireorders.domain.collections;

import com.faire.orders.faireorders.domain.Order;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderList implements EntityList<Order>{
    private int page;
    private int limit;
    private List<Order> orders;

    @Override
    public List<Order> getItems() {
        return orders;
    }
}
