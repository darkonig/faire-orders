package com.faire.orders.faireorders.repository;

import com.faire.orders.faireorders.model.ProductOptionLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionLogRepository extends CrudRepository<ProductOptionLog, String> {
}
