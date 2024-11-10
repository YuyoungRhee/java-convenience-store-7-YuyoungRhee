package store.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.dto.OrderCheckDto;
import store.dto.OrderResultDto;

public class Inventory {
    private final Map<String, List<Product>> stock;

    public Inventory(Map<String, List<Product>> initialStock) {
        this.stock = new HashMap<>(initialStock);
    }

    public OrderCheckDto checkInventory(String productName, int requestedQuantity) {
        List<Product> products = stock.get(productName);

        if (products == null) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다.");
        }

        int totalStock = products.stream()
                .mapToInt(Product::getQuantity)
                .sum();
        boolean isEnough = totalStock >= requestedQuantity;

        int availableGiftQuantity = 0;
        int noPromotionQuantity = 0;
        int remainingQuantity = requestedQuantity;

        int promotionQuantity = 0;

        for (Product product : products) {
            int usedQuantity = Math.min(remainingQuantity, product.getQuantity());
            availableGiftQuantity += product.calculateAdditionalGiftQuantity(usedQuantity);
            noPromotionQuantity += product.excludeDiscountQuantity(usedQuantity);

            promotionQuantity += product.getPromotionQuantity(remainingQuantity);
            remainingQuantity -= usedQuantity;

            if (remainingQuantity <= 0) {
                break;
            }
        }

        if (promotionQuantity == 0) {
            noPromotionQuantity = 0;
        }

        return new OrderCheckDto(productName, isEnough, availableGiftQuantity, noPromotionQuantity);
    }

    public OrderResultDto processOrder(String productName, int requestQuantity) {
        List<Product> products = stock.get(productName);

        int giftQuantity = 0;
        int totalPrice = 0;
        int promotionDiscountedPrice = 0;
        int remainingQuantity = requestQuantity;

        for (Product product : products) {
            int usedQuantity = Math.min(remainingQuantity, product.getQuantity());

            giftQuantity += product.calculateGiftQuantity(usedQuantity);
            totalPrice += product.calculateTotalPrice(usedQuantity);
            promotionDiscountedPrice += product.calculateDiscountedPrice(usedQuantity);
            remainingQuantity -= usedQuantity;

            product.reduceQuantity(usedQuantity);

            if (remainingQuantity <= 0) {
                break;
            }
        }
        return new OrderResultDto(productName, requestQuantity, giftQuantity, totalPrice, promotionDiscountedPrice);
    }

}
