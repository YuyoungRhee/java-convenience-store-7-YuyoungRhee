package store.domain;

import store.domain.promotion.Promotion;

public class Product {
    private final String name;
    private final int price;
    private int quantity;
    private final Promotion promotion;

    public Product(String name, int price, int quantity, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public int calculateTotalPrice(int quantity) {
        return price * quantity;
    }

    public void reduceQuantity(int requestQuantity) {
        //예외처리?
        int reducedQuantity = Math.min(quantity, requestQuantity);

        quantity -= reducedQuantity;
    }

    public int getPromotionQuantity(int requestQuantity) {
        if(promotion.isActive()) {
            return Math.min(requestQuantity, quantity);
        }
        return 0;
    }

    public int calculateDiscountedPrice(int requestQuantity) {
        if (promotion.isActive()) {
            int giftQuantity = promotion.calculateGiftQuantity(requestQuantity);
            int payableQuantity = requestQuantity - giftQuantity;

            return price * payableQuantity;
        }
        return 0;
    }

    public int calculateGiftQuantity(int requestQuantity) {
        return promotion.calculateGiftQuantity(requestQuantity);
    }

    public int calculateAdditionalGiftQuantity(int requestedQuantity) {
        return promotion.calculateAvailableGiftQuantity(requestedQuantity);
    }

    public int excludeDiscountQuantity(int requestedQuantity) {
        return promotion.excludeDiscountQuantity(requestedQuantity);
    }

    public int getQuantity() {
        return quantity;
    }

}
