package com.faire.orders.faireorders.controller;

import com.faire.orders.faireorders.domain.Order;
import com.faire.orders.faireorders.domain.Product;
import com.faire.orders.faireorders.entity.AnalyzesResult;
import com.faire.orders.faireorders.entity.ProcessOrderResult;
import com.faire.orders.faireorders.exception.TechnicalException;
import com.faire.orders.faireorders.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService service;
    private final ObjectMapper mapper;

    public OrderController(OrderService service, ObjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping("/{brandId}")
    public ResponseEntity<AnalyzesResult> processOrders(@RequestHeader("X-FAIRE-ACCESS-TOKEN") String accessToken, @PathVariable("brandId") String brandId) {
        logger.debug("[Controller] Request processOrders brandId: {} ", brandId);

        List<Order> orders = service.getNewOrders(accessToken);

        try {
            logger.debug(mapper.writeValueAsString(orders));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (orders.isEmpty()) {
            logger.info("No new orders found.");
            throw new TechnicalException("No new orders found.");
        }

        List<Product> products = service.getProducts(accessToken, brandId);

        try {
            logger.debug(mapper.writeValueAsString(products));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (products.isEmpty()) {
            logger.info("No products found with this brandId.");
            throw new TechnicalException("No products found with this brandId.");
        }

        ProcessOrderResult result = service.processOrder(accessToken, orders, products);
        logger.debug("[Controller] Result: {}", result);

        AnalyzesResult analytics = service.getResultAnalytics(result);
        logger.debug("[Controller] AnalyzesResult: {}", analytics);

        return ResponseEntity.ok(analytics);
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
