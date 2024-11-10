package store.domain.order;

import store.domain.Inventory;

public class OrderFactory {
    private final Inventory inventory;

    public OrderFactory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Order createOrder(String productName, int purchaseQuantity) {
        OrderChecker orderChecker = new OrderChecker(inventory);
        OrderProcessor orderProcessor = new OrderProcessor(inventory);
        return new Order(productName, purchaseQuantity, orderChecker, orderProcessor);
    }

}