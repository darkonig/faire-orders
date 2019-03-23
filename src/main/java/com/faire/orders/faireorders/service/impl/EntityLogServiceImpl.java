package com.faire.orders.faireorders.service.impl;

import com.faire.orders.faireorders.model.OrderItemLog;
import com.faire.orders.faireorders.model.OrderLog;
import com.faire.orders.faireorders.model.ProductOptionLog;
import com.faire.orders.faireorders.repository.OrderItemLogRepository;
import com.faire.orders.faireorders.repository.OrderLogRepository;
import com.faire.orders.faireorders.repository.ProductOptionLogRepository;
import com.faire.orders.faireorders.service.EntityLogService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class EntityLogServiceImpl implements EntityLogService {
    private final OrderLogRepository orderLogRepository;
    private final OrderItemLogRepository orderItemLogRepository;
    private final ProductOptionLogRepository productOptionLogRepository;

    public EntityLogServiceImpl(OrderLogRepository orderLogRepository, OrderItemLogRepository orderItemLogRepository, ProductOptionLogRepository productOptionLogRepository) {
        this.orderLogRepository = orderLogRepository;
        this.orderItemLogRepository = orderItemLogRepository;
        this.productOptionLogRepository = productOptionLogRepository;
    }

    @Override
    public OrderLog save(OrderLog entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setUpdatedAt(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")));
        }
        return orderLogRepository.save(entity);
    }

    @Override
    public Iterable<OrderItemLog> saveAll(List<OrderItemLog> entity) {
        entity.stream().forEach(e -> {
            if (StringUtils.isEmpty(e.getId())) {
                e.setUpdatedAt(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")));
            }
        });

        return orderItemLogRepository.saveAll(entity);
    }

    @Override
    public ProductOptionLog save(ProductOptionLog entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setUpdatedAt(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")));
        }
        return productOptionLogRepository.save(entity);
    }
}
