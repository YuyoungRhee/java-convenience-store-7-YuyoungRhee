package store.domain;

import store.dto.OrderCheckDto;
import store.dto.OrderResultDto;

public class Order {
    private final String productName;
    private int purchaseQuantity;
    private boolean isConfirmNoPromotion = false;
    private boolean isConfirmAvailableGift = false;

    public Order(String productName, int purchaseQuantity) {
        this.productName = productName;
        this.purchaseQuantity = purchaseQuantity;
    }

    //상태 변경 로직
    public void addAvailableGiftQuantity(int quantity) {
        this.purchaseQuantity += quantity;
    }

    public void setConfirmNoPromotion() {
        this.isConfirmNoPromotion = true;
    }

    public void setConfirmAvailableGift() {
        this.isConfirmAvailableGift = true;
    }

    //getter
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

    public void excludeNoPromotionQuantity(int quantity) {
        purchaseQuantity -= quantity;
    }
}
