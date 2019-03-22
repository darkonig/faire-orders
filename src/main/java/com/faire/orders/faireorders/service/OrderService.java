package com.faire.orders.faireorders.service;

import com.faire.orders.faireorders.domain.Order;
import com.faire.orders.faireorders.domain.Product;
import com.faire.orders.faireorders.entity.AnalyzesResult;
import com.faire.orders.faireorders.entity.ProcessOrderResult;

import java.util.List;

public interface OrderService {

    List<Product> getProducts(String accessToken, String brandId);

    List<Order> getNewOrders(String accessToken);

    ProcessOrderResult processOrder(String accessToken, List<Order> orders, List<Product> brandProductList);

    AnalyzesResult getResultAnalytics(ProcessOrderResult result);
}
