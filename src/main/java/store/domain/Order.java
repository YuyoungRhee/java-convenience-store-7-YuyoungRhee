package store.domain;

import store.dto.OrderCheckDto;

public class Order {
    private final String productName;
    private int purchaseQuantity;
    private boolean isConfirmNoPromotion = false;
    private boolean isConfirmAvailableGift = false;
    private final OrderValidator orderValidator;

    public Order(String productName, int purchaseQuantity, OrderValidator orderValidator) {
        this.productName = productName;
        this.purchaseQuantity = purchaseQuantity;
        this.orderValidator = orderValidator;
    }

    public OrderCheckDto validateOrder() {
        return orderValidator.checkOrder(productName, purchaseQuantity);
    }

    public void applyAvailableGift(int availableGiftQuantity, boolean addGift) {
        if (addGift && availableGiftQuantity > 0) {
            addAvailableGiftQuantity(availableGiftQuantity);
        }
        isConfirmAvailableGift = true;
    }

    public void applyNoPromotionQuantity(int noPromotionQuantity, boolean confirmRegularPrice) {
        if (!confirmRegularPrice && noPromotionQuantity > 0) {
            excludeNoPromotionQuantity(noPromotionQuantity);
        }
        isConfirmNoPromotion = true;
    }

    // 상태 변경 로직
    public void addAvailableGiftQuantity(int quantity) {
        this.purchaseQuantity += quantity;
    }

    public void excludeNoPromotionQuantity(int quantity) {
        purchaseQuantity -= quantity;
    }


    // getter
    public String getProductName() {
        return productName;
    }

    public int getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public boolean isConfirmAvailableGift() {
        return isConfirmAvailableGift;
    }

    public boolean isConfirmNoPromotion() {
        return isConfirmNoPromotion;
    }

}