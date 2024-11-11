package store.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.dto.OrderResultDto;

public class Inventory {
    private final Map<String, List<Product>> stock;

    public Inventory(Map<String, List<Product>> initialStock) {
        this.stock = new HashMap<>(initialStock);
    }

    public int getAvailableStock(String productName) {
        List<Product> products = stock.get(productName);

        if (products == null) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다: " + productName);
        }

        return products.stream()
                .mapToInt(Product::getQuantity)
                .sum();
    }

    //getter
    public List<Product> getProducts(String productName) {
        return stock.getOrDefault(productName, Collections.emptyList());
    }

    public OrderResultDto processOrder(String productName, int requestQuantity) {
        List<Product> products = stock.get(productName);

        int giftQuantity = calculateGiftQuantity(products, requestQuantity);
        int totalPrice = calculateTotalPrice(products, requestQuantity);
        int promotionDiscountedPrice = calculatePromotionDiscount(products, requestQuantity);
        reduceProductQuantity(products, requestQuantity);

        return new OrderResultDto(productName, requestQuantity, giftQuantity, totalPrice, promotionDiscountedPrice);
    }

    private int calculateGiftQuantity(List<Product> products, int requestQuantity) {
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

    private int calculateTotalPrice(List<Product> products, int requestQuantity) {
        int totalPrice = 0;
        int remainingQuantity = requestQuantity;

        for (Product product : products) {
            int usedQuantity = Math.min(remainingQuantity, product.getQuantity());
            totalPrice += product.calculateTotalPrice(usedQuantity);
            remainingQuantity -= usedQuantity;
            if (remainingQuantity <= 0) break;
        }

        return totalPrice;
    }

    private int calculatePromotionDiscount(List<Product> products, int requestQuantity) {
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

    private void reduceProductQuantity(List<Product> products, int requestQuantity) {
        int remainingQuantity = requestQuantity;

        for (Product product : products) {
            int usedQuantity = Math.min(remainingQuantity, product.getQuantity());
            product.reduceQuantity(usedQuantity);
            remainingQuantity -= usedQuantity;
            if (remainingQuantity <= 0) break;
        }
    }

}
