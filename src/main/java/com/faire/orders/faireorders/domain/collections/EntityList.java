package com.faire.orders.faireorders.domain.collections;

import java.util.List;

public interface EntityList<T> {
    int getLimit();

    int getPage();

    List<T> getItems();
}
