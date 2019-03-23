package com.faire.orders.faireorders.service;

import com.faire.orders.faireorders.domain.*;
import com.faire.orders.faireorders.service.entity.AnalyzesResult;
import com.faire.orders.faireorders.service.entity.ProcessOrderResult;
import com.faire.orders.faireorders.service.impl.AnalyzeServiceImpl;
import org.junit.Before;
import org.junit.Test;

import javax.xml.ws.spi.WebServiceFeatureAnnotation;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class AnalyzeServiceTest {

    private AnalyzeService service;
    private List<Order> newOrderList;
    private List<Product> brandProductList;
    private String brandId = "b_0123";

    @Before
    public void setUp() throws Exception {
        service = new AnalyzeServiceImpl();

        brandProductList = Arrays.asList(
                Product.builder().brandId(brandId).id("p_1")
                        .options(Arrays.asList(
                                ProductOption.builder()
                                        .id("po_bart")
                                        .active(true)
                                        .availableQuantity(10)
                                        .build(),
                                ProductOption.builder()
                                        .id("po_lisa")
                                        .active(true)
                                        .availableQuantity(5)
                                        .build()
                        ))
                        .build(),
                Product.builder().brandId(brandId).id("p_2")
                        .options(Arrays.asList(
                                ProductOption.builder()
                                        .id("po_bart1")
                                        .active(true)
                                        .availableQuantity(0)
                                        .build(),
                                ProductOption.builder()
                                        .id("po_lisa1")
                                        .active(true)
                                        .availableQuantity(1)
                                        .build()
                        ))
                        .build(),
                Product.builder().brandId(brandId).id("p_3")
                        .options(Arrays.asList(
                                ProductOption.builder()
                                        .id("po_bart2")
                                        .active(true)
                                        .availableQuantity(5)
                                        .build(),
                                ProductOption.builder()
                                        .id("po_lisa2")
                                        .active(true)
                                        .availableQuantity(2)
                                        .build()
                        ))
                        .build()
        );

        newOrderList = Arrays.asList(
                Order.builder()
                        .id("bo_123")
                        .state(OrderState.NEW)
                        .items(Arrays.asList(
                                OrderItem.builder()
                                        .id("oi_1230")
                                        .productId("p_1")
                                        .productOptionId("po_bart")
                                        .quantity(10)
                                        .priceCents(100)
                                        .includesTester(true)
                                        .testerPriceCents(25)
                                        .build(),
                                OrderItem.builder()
                                        .id("oi_1231")
                                        .productId("p_1")
                                        .productOptionId("po_lisa")
                                        .quantity(5)
                                        .priceCents(50)
                                        .build(),
                                OrderItem.builder()
                                        .id("oi_1232")
                                        .productId("p_2")
                                        .productOptionId("po_lisa1")
                                        .quantity(5)
                                        .priceCents(500)
                                        .build()
                                )

                        )
                        .address(Address.builder()
                                .state("Ontario")
                                .build())
                        .build()
        );

    }

    @Test
    public void getResultAnalytics() {
        Map<String, Map<String, BackorderItem>> backorders = new HashMap<>();

        OrderItem orderItem = newOrderList.get(0).getItems().get(0);
        orderItem.setProcessed(true);
        newOrderList.get(0).getItems().get(1).setProcessed(true);

        Map<String, BackorderItem> items = new HashMap<>();
        items.put(orderItem.getOrderId(), BackorderItem.builder()
                .orderItem(orderItem)
                .availableQuantity(1)
                .build());

        backorders.put(newOrderList.get(0).getId(), items);

        Map<String, ProductOptionUpdateRequest> prodOpt = new HashMap<>();

        List<ProductOption> options = brandProductList.get(0).getOptions();
        ProductOption opt1 = options.get(1);
        prodOpt.put(opt1.getId(), ProductOptionUpdateRequest.builder()
                .productOption(opt1)
                .availableUnits(0)
                .soldUnits(5)
                .totalPrice(0)
                .build());

        ProductOption opt2 = options.get(0);
        prodOpt.put(opt2.getId(), ProductOptionUpdateRequest.builder()
                .productOption(opt2)
                .availableUnits(0)
                .soldUnits(10)
                .totalPrice(25)
                .testerTotalPrice(250)
                .build());

        List<Order> orders = new ArrayList<>();
        orders.add(newOrderList.get(0));

        ProcessOrderResult result = ProcessOrderResult.builder()
                .backorders(backorders)
                .productOptionUpdate(prodOpt)
                .orders(orders)
                .build();

        AnalyzesResult analytics = service.getResultAnalytics(result);

        assertThat(analytics.getBestSellingProduct().getOption().getId())
                .isEqualTo("po_bart");
        assertThat(analytics.getBestSellingProduct().getUnits())
                .isEqualTo(10);

        assertThat(analytics.getLargestOrder().getOrder().getId())
                .isEqualTo("bo_123");
        assertThat(analytics.getLargestOrder().getValue())
                .isEqualTo(1_250);

        assertThat(analytics.getStateGreatestOrders().getState())
                .isEqualTo("Ontario");
        assertThat(analytics.getStateGreatestOrders().getValue())
                .isEqualTo(1_250);

        assertThat(analytics.getStateMostOrders().getState())
                .isEqualTo("Ontario");
        assertThat(analytics.getStateMostOrders().getValue())
                .isEqualTo(15);

        assertThat(analytics.getOrderWithGreatestTests().getOrder().getId())
                .isEqualTo("bo_123");
        assertThat(analytics.getOrderWithGreatestTests().getValue())
                .isEqualTo(250);
    }
}