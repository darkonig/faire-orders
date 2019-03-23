package com.faire.orders.faireorders.service;

import com.faire.orders.faireorders.model.OrderItemLog;
import com.faire.orders.faireorders.model.OrderLog;
import com.faire.orders.faireorders.model.ProductOptionLog;

import java.util.List;

public interface EntityLogService {

    OrderLog save(OrderLog entity);

    Iterable<OrderItemLog> saveAll(List<OrderItemLog> entity);

    ProductOptionLog save(ProductOptionLog entity);

}
