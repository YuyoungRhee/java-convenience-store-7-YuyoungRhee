package store.domain.order;

import store.domain.Inventory;
import store.dto.OrderCheckDto;
import store.dto.OrderResultDto;

public class Order {
    private final String productName;
    private int purchaseQuantity;
    private final Inventory inventory;

    public Order(String productName, int purchaseQuantity, Inventory inventory) {
        this.productName = productName;
        this.purchaseQuantity = purchaseQuantity;
        this.inventory = inventory;
    }

    public OrderCheckDto checkOrder() {
        return inventory.checkInventory(productName, purchaseQuantity);
    }

    public OrderResultDto processOrder() {
        return inventory.processOrder(productName, purchaseQuantity);
    }

    public void addAvailableGiftQuantity(int quantity) {
        purchaseQuantity += quantity;
    }

    public void excludeNoPromotionQuantity(int quantity) {
        purchaseQuantity -= quantity;
    }

}
