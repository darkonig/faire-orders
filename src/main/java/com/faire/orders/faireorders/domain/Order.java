package com.faire.orders.faireorders.domain;

import java.util.List;

public class Order {
    private String id;
    private String createdAt;
    private String updatedAt;
    private String state;
    private List<OrderItem> items;
    private List<Shipment> shipments;
    private Address address;
}
