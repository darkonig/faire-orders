package com.faire.orders.faireorders.service;

import com.faire.orders.faireorders.domain.OrderState;
import com.faire.orders.faireorders.model.OrderItemLog;
import com.faire.orders.faireorders.model.OrderLog;
import com.faire.orders.faireorders.model.ProductOptionLog;
import com.faire.orders.faireorders.repository.OrderItemLogRepository;
import com.faire.orders.faireorders.repository.OrderLogRepository;
import com.faire.orders.faireorders.repository.ProductOptionLogRepository;
import com.faire.orders.faireorders.service.impl.EntityLogServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

//@RunWith(MockitoJUnitRunner.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class EntityLogServiceTest {
    @Autowired
    private EntityLogService service;

    @Test
    public void saveOrderLog() {
        OrderLog entity = service.save(OrderLog.builder()
                .orderId("abc")
                .status(OrderState.NEW)
                .build());

        assertThat(entity.getUpdatedAt()).isNotNull();
    }

    @Test
    public void saveAll() {
        Iterable<OrderItemLog> entities = service.saveAll(Arrays.asList(
                OrderItemLog.builder()
                        .oiId("oi_123")
                        .includesTester(true)
                        .testerPriceCents(123)
                        .build()
                , OrderItemLog.builder()
                        .oiId("oi_124")
                        .includesTester(true)
                        .testerPriceCents(123)
                        .build()
        ));

        assertThat(entities).filteredOn(e -> e.getUpdatedAt() != null);
    }

    @Test
    public void saveProductOptionsLog() {
        ProductOptionLog entity = service.save(ProductOptionLog.builder()
                .optionId("abc")
                .productId("12")
                .build());

        assertThat(entity.getUpdatedAt()).isNotNull();
    }
}