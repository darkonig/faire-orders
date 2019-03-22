package com.faire.orders.faireorders.domain.collections;

import com.faire.orders.faireorders.domain.Inventory;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {

    private Set<Inventory> inventories;

    public boolean add (Inventory inventory) {
        if (inventories == null) {
            inventories = new HashSet<>();
        }
        return inventories.add(inventory);
    }


}
