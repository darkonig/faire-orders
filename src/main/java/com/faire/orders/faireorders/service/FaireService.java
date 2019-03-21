package com.faire.orders.faireorders.service;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="faireServices", url = "${faireservice.url}")
public interface FaireService {

    @RequestLine("GET /api/documents/{contentType}")
    @Headers({"Content-Type: application/json", "X-Ping: {token}"})
    void getOrders(@Param("token") String token);

}
