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
        int reducedQuantity = Math.min(quantity, requestQuantity);

        quantity -= reducedQuantity;
    }

    public int getPromotionQuantity(int requestQuantity) {
        if(promotion.isActive()) {
            int usedQuantity = Math.min(requestQuantity, quantity);
            return usedQuantity - promotion.getNonDiscountableQuantity(usedQuantity);
        }
        return 0;
    }

    public int calculateDiscountedPrice(int requestQuantity) {
        if (promotion.isActive()) {
            int giftQuantity = promotion.calculateGiftQuantity(requestQuantity);

            return price * giftQuantity;
        }
        return 0;
    }

    public int calculateGiftQuantity(int requestQuantity) {
        return promotion.calculateGiftQuantity(requestQuantity);
    }

    public int calculateAdditionalGiftQuantity(int requestedQuantity) {
        return promotion.calculateAdditionalGiftQuantity(requestedQuantity);
    }

    public int excludeDiscountQuantity(int requestedQuantity) {
        return promotion.getNonDiscountableQuantity(requestedQuantity);
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Promotion getPromotion() {
        return promotion;
    }
}
