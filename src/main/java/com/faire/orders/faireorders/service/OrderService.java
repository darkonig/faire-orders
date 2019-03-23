package com.faire.orders.faireorders.service;

import com.faire.orders.faireorders.domain.Order;
import com.faire.orders.faireorders.domain.Product;
import com.faire.orders.faireorders.service.entity.AnalyzesResult;
import com.faire.orders.faireorders.service.entity.ProcessOrderResult;

import java.util.List;

public interface OrderService {

    List<Product> getProducts(String accessToken, String brandId);

    List<Order> getNewOrders(String accessToken);

    ProcessOrderResult processOrder(String accessToken, List<Order> orders, List<Product> brandProductList);

}
