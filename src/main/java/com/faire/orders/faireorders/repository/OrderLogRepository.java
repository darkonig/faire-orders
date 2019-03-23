package com.faire.orders.faireorders.repository;

import com.faire.orders.faireorders.model.OrderLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLogRepository extends CrudRepository<OrderLog, String> {
}
