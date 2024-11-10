package store.domain.order;

import store.dto.OrderCheckDto;
import store.dto.OrderResultDto;

public class Order {
    private final String productName;
    private int purchaseQuantity;
    private final OrderChecker orderChecker;
    private final OrderProcessor orderProcessor;

    public Order(String productName, int purchaseQuantity, OrderChecker orderChecker, OrderProcessor orderProcessor) {
        this.productName = productName;
        this.purchaseQuantity = purchaseQuantity;
        this.orderChecker = orderChecker;
        this.orderProcessor = orderProcessor;
    }

    public OrderCheckDto checkOrder() {
        return orderChecker.checkOrder(productName, purchaseQuantity);
    }

    public OrderResultDto processOrder() {
        return orderProcessor.processOrder(productName, purchaseQuantity);
    }

    public void addAvailableGiftQuantity(int quantity) {
        purchaseQuantity += quantity;
    }

    public void excludeNoPromotionQuantity(int quantity) {
        purchaseQuantity -= quantity;
    }

}
