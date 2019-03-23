package com.faire.orders.faireorders.repository;

import com.faire.orders.faireorders.model.OrderItemLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemLogRepository extends CrudRepository<OrderItemLog, String> {
}
