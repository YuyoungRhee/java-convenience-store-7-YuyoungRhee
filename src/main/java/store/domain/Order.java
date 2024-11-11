package store.domain;

import store.dto.OrderCheckDto;
import store.dto.OrderResultDto;

public class Order {
    private final String productName;
    private int purchaseQuantity;
    private final Inventory inventory;
    private final OrderValidator orderValidator;

    public Order(String productName, int purchaseQuantity, Inventory inventory, OrderValidator orderValidator) {
        this.productName = productName;
        this.purchaseQuantity = purchaseQuantity;
        this.inventory = inventory;
        this.orderValidator = orderValidator;
    }

    public OrderCheckDto checkOrder() {
        return orderValidator.checkOrder(productName, purchaseQuantity);
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
