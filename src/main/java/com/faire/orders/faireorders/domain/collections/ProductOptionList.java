package com.faire.orders.faireorders.domain.collections;

import com.faire.orders.faireorders.domain.ProductOption;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductOptionList {

    private List<ProductOption> options;

}
