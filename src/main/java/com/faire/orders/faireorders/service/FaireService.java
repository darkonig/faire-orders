package com.faire.orders.faireorders.service;

import com.faire.orders.faireorders.domain.*;
import com.faire.orders.faireorders.domain.collections.OrderResponse;
import com.faire.orders.faireorders.domain.collections.ProductResponse;
import feign.CollectionFormat;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@FeignClient(name = "faireServices", url = "${faireservice.url}")
@Service
public interface FaireService {

    @RequestLine("GET /api/v1/products?page={page}")
    @Headers("X-FAIRE-ACCESS-TOKEN: {token}")
    ProductResponse getProducts(@Param("token") String accessToken, @Param("page") int page);

    @RequestLine(value="GET /api/v1/orders?page={page}&excluded_states={excluded_states}&ship_after_max={ship_after_max}"
            , collectionFormat = CollectionFormat.CSV)
    @Headers("X-FAIRE-ACCESS-TOKEN: {token}")
    OrderResponse getOrders(@Param("token") String accessToken, @Param("page") int page
            , @Param("excluded_states") List<OrderState> excludedStates, @Param("ship_after_max") String shipAfter);

    @RequestLine("PATCH /api/v1/products/options/{id}")
    @Headers("X-FAIRE-ACCESS-TOKEN: {token}")
    ProductOption updateProductOption(@Param("token") String accessToken
            , @Param("id") String optionId
            , ProductOptionUpdateRequest body);

    /*@PatchMapping("/api/v1/products/options/inventory-levels")
    @ResponseBody()
    ProductOptionResponse updateInventory(@RequestHeader("X-FAIRE-ACCESS-TOKEN") String accessToken
            , @RequestBody InventoryResponse inventory);*/

    @RequestLine("PUT /api/v1/orders/{id}/processing")
    @Headers("X-FAIRE-ACCESS-TOKEN: {token}")
    boolean acceptOrder(@Param("token") String accessToken, @Param("id") String orderId);

    @RequestLine("POST /api/v1/orders/{id}/items/availability")
    @Headers("X-FAIRE-ACCESS-TOKEN: {token}")
    Order backorderItems(@Param("token") String accessToken
            , @Param("id") String orderId
            , Map<String, BackorderItem> body);
}
