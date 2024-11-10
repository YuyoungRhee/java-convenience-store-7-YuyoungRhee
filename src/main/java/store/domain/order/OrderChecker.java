package store.domain.order;

import store.domain.Inventory;
import store.dto.OrderCheckDto;

public class OrderChecker {
    private final Inventory inventory;

    public OrderChecker(Inventory inventory) {
        this.inventory = inventory;
    }

    public OrderCheckDto checkOrder(String productName, int purchaseQuantity) {
        return inventory.checkInventory(productName, purchaseQuantity);
    }
}
