package store.service;

import java.util.List;
import store.domain.Product;


public class PromotionCalculator {

    public int calculateGiftQuantity(List<Product> products, int requestQuantity) {
        int giftQuantity = 0;
        int remainingQuantity = requestQuantity;

        for (Product product : products) {
            int usedQuantity = Math.min(remainingQuantity, product.getQuantity());
            giftQuantity += product.calculateGiftQuantity(usedQuantity);
            remainingQuantity -= usedQuantity;
            if (remainingQuantity <= 0) break;
        }

        return giftQuantity;
    }

    public int calculatePromotionDiscount(List<Product> products, int requestQuantity) {
        int promotionDiscountedPrice = 0;
        int remainingQuantity = requestQuantity;

        for (Product product : products) {
            int usedQuantity = Math.min(remainingQuantity, product.getQuantity());
            promotionDiscountedPrice += product.calculateDiscountedPrice(usedQuantity);
            remainingQuantity -= usedQuantity;
            if (remainingQuantity <= 0) break;
        }

        return promotionDiscountedPrice;
    }

    public int calculateNoPromotionQuantity(List<Product> products, int requestQuantity) {
        int promotionQuantity = 0;
        int remainingQuantity = requestQuantity;

        for (Product product : products) {
            if (product.isPromotionProduct()) {
                promotionQuantity += product.getQuantity();
            }
        }

        return Math.max(0, requestQuantity - promotionQuantity);
    }

}
