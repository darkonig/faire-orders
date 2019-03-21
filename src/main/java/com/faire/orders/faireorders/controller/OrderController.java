package com.faire.orders.faireorders.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders")
public class OrderController {

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProducts(@PathVariable("") String productId) {
        // get all products of the brand
        // get inventory level for each prod option
        // save invetory

        // get all orders
            // if invetory level > num order
                // accepts
                // update invetory levels (prod options) when orders moves to processing
            // else
                // not enough invetory to ordered items (To those who don`t fave)



        return null;
    }

}
