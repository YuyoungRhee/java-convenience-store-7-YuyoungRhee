package store.domain.promotion;

import store.domain.promotionCondition.PromotionCondition;

public class Promotion {
    private final int purchaseQuantity;
    private final int giftQuantity;
    private final PromotionCondition promotionCondition;

    public Promotion(int purchaseQuantity, int giftQuantity, PromotionCondition promotionCondition) {
        this.purchaseQuantity = purchaseQuantity;
        this.giftQuantity = giftQuantity;
        this.promotionCondition = promotionCondition;
    }

    public boolean isActive() {
        return promotionCondition.isSatisfiedBy();
    }
    public int calculateGiftQuantity(int quantity) {
        if (!isActive()) {
            return 0;
        }

        int promotionUnit = purchaseQuantity + giftQuantity;
        return (quantity / promotionUnit) * giftQuantity;
    }

    public int calculateAvailableGiftQuantity(int quantity) {
        if (!isActive()) {
            return 0;
        }
        int promotionUnit = purchaseQuantity + giftQuantity;
        int additionalQuantityNeeded = promotionUnit - (quantity % promotionUnit);

        if (additionalQuantityNeeded <= giftQuantity) {
            return additionalQuantityNeeded;
        }
        return 0;

    }

    public int excludeDiscountQuantity(int quantity) {
        if (!isActive()) {
            return quantity;
        }

        int promotionUnit = purchaseQuantity + giftQuantity;
        return quantity % promotionUnit;
    }

}
