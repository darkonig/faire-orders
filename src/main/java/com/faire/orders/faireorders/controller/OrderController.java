package com.faire.orders.faireorders.controller;

import com.faire.orders.faireorders.domain.Order;
import com.faire.orders.faireorders.domain.Product;
import com.faire.orders.faireorders.entity.AnalyzesResult;
import com.faire.orders.faireorders.entity.ProcessOrderResult;
import com.faire.orders.faireorders.exception.TechnicalException;
import com.faire.orders.faireorders.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("orders")
public class OrderController {

    private final OrderService service;
    private final ObjectMapper mapper;

    public OrderController(OrderService service, ObjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping("/{brandId}")
    public ResponseEntity<AnalyzesResult> getProducts(@RequestHeader("X-FAIRE-ACCESS-TOKEN") String accessToken, @PathVariable("brandId") String brandId) {
        List<Product> products = service.getProducts(accessToken, brandId);

        try {
            System.out.println(mapper.writeValueAsString(products));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (products.isEmpty()) {
            throw new TechnicalException("No products found with this brandId.");
        }

        List<Order> orders = service.getNewOrders(accessToken);

        try {
            System.out.println(mapper.writeValueAsString(orders));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (orders.isEmpty()) {
            throw new TechnicalException("No new orders found.");
        }

//        ProcessOrderResult result = service.processOrder(accessToken, orders, products);
//
//        AnalyzesResult analytics = service.getResultAnalytics(result);

        //return ResponseEntity.ok(analytics);
        return null;
        // get all products of the brand
        // get inventory level for each prod option
        // save inventory

        // get all orders
            // if invetory level > num order
                // accepts
                // update invetory levels (prod options) when orders moves to processing
            // else
                // not enough invetory to ordered items (To those who don`t fave)
    }

}
