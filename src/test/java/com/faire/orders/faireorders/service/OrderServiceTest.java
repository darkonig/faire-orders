package com.faire.orders.faireorders.service;

import com.faire.orders.faireorders.domain.*;
import com.faire.orders.faireorders.domain.collections.OrderResponse;
import com.faire.orders.faireorders.domain.collections.ProductResponse;
import com.faire.orders.faireorders.entity.AnalyzesResult;
import com.faire.orders.faireorders.entity.ProcessOrderResult;
import com.faire.orders.faireorders.exception.TechnicalException;
import com.faire.orders.faireorders.service.impl.OrderServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @Mock
    private FaireService faireService;

    private OrderService service;
    private List<Product> brandProductList;
    private List<Product> productList;
    private List<Order> newOrderList;
    private List<Order> orderList;

    private String brandId = "b_123";
    private String accessToken = "TOKENABC";

    @Before
    public void setUp() throws Exception {
        service = new OrderServiceImpl(faireService);

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

        productList = new ArrayList<>(brandProductList);
        productList.add(Product.builder().brandId("b_9877").id("p_1234").build());
        productList.add(Product.builder().brandId("b_9877").id("p_5678").build());

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

        orderList = new ArrayList<>(newOrderList);
        orderList.add(Order.builder()
                        .id("bo_1234")
                        .state(OrderState.DELIVERED)
                        .items(
                                Arrays.asList(OrderItem.builder()
                                        .id("oi_124")
                                        .productOptionId("p_abcu")
                                        .quantity(10)
                                        .build())
                        )
                    .address(Address.builder()
                            .state("Quebec")
                            .build())
                        .build()
        );

        when(faireService.getProducts(anyString(), eq(1))).thenReturn(ProductResponse.builder()
                .limit(50)
                .page(1)
                .products(productList)
                .build());
        when(faireService.getProducts(anyString(), eq(2))).thenReturn(ProductResponse.builder()
                .limit(50)
                .page(2)
                .products(Collections.emptyList())
                .build());

        when(faireService.getOrders(anyString(), eq(1), anyList(), anyString())).thenReturn(OrderResponse.builder()
                .limit(10)
                .page(2)
                .orders(orderList)
                .build());
        when(faireService.getOrders(anyString(), eq(2), anyList(), anyString())).thenReturn(OrderResponse.builder()
                .limit(10)
                .page(2)
                .orders(Collections.emptyList())
                .build());
    }

    @Test
    public void getProducts() {
        List<Product> products = service.getProducts(accessToken, brandId);

        assertThat(products)
                //.withFailMessage("The products list is invalid.")
                .hasSize(brandProductList.size())
                .filteredOn(e -> brandId.equals(e.getBrandId()));
    }

    @Test
    public void getNewOrders() {
        List<Order> orders = service.getNewOrders(accessToken);

        assertThat(orders)
                .hasSize(newOrderList.size())
                .filteredOn(e -> e.getState() == OrderState.NEW);
    }

    @Test
    public void processOrder_success() {
        ProcessOrderResult orders = service.processOrder(accessToken, newOrderList, brandProductList);

        assertThat(orders.getOrders())
                .extracting(Order::getId)
                .contains(newOrderList.stream().map(Order::getId)
                        .collect(Collectors.joining()));

        Map<String, BackorderItem> backorder = orders.getBackorders().get("bo_123");
        assertThat(backorder.size()).isEqualTo(1);

        BackorderItem backorderItem = backorder.get("oi_1232");
        assertThat(backorderItem).isNotNull();

        assertThat(backorderItem)
                .extracting(BackorderItem::getAvailableQuantity)
                .isEqualTo(1);
        assertThat(backorderItem)
                .extracting(BackorderItem::isDiscontinued)
                .isEqualTo(false);

        assertThat(orders.getProductOptionUpdate())
                .hasSize(2);
    }

    @Test
    public void getResultAnalytics() {
        ProcessOrderResult result = service.processOrder(accessToken, newOrderList, brandProductList);

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

    @Test(expected = TechnicalException.class)
    public void processOrder_error() {
        service.processOrder(accessToken, orderList, brandProductList);
    }
}