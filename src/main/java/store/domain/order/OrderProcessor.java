package store.domain.order;

import store.domain.Inventory;
import store.dto.OrderResultDto;

public class OrderProcessor {
    private final Inventory inventory;

    public OrderProcessor(Inventory inventory) {
        this.inventory = inventory;
    }

    public OrderResultDto processOrder(String productName, int purchaseQuantity) {
        return inventory.processOrder(productName, purchaseQuantity);
    }
}
