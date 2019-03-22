package com.faire.orders.faireorders.domain.collections;

import com.faire.orders.faireorders.domain.Product;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductResponse implements EntityList<Product> {
    private int page;
    private int limit;
    private List<Product> products;

    @Override
    public List<Product> getItems() {
        return products;
    }
}
