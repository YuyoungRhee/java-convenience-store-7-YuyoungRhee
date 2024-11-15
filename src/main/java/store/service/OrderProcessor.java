package store.service;

import java.util.List;
import store.domain.Inventory;
import store.domain.order.Order;
import store.domain.Product;
import store.dto.OrderResultDto;

public class OrderProcessor {
    private final Inventory inventory;
    private final PromotionCalculator promotionCalculator;

    public OrderProcessor(Inventory inventory, PromotionCalculator promotionCalculator) {
        this.inventory = inventory;
        this.promotionCalculator = promotionCalculator;
    }

    public OrderResultDto processOrder(Order order) {
        List<Product> products = inventory.getProducts(order.getProductName());

        int giftQuantity = promotionCalculator.calculateGiftQuantity(products, order.getPurchaseQuantity());
        int promotionDiscountedPrice = promotionCalculator.calculatePromotionDiscount(products, order.getPurchaseQuantity());
        int totalPrice = calculateTotalPrice(products, order.getPurchaseQuantity());

        inventory.reduceProductQuantity(products, order.getPurchaseQuantity());

        return new OrderResultDto(order.getProductName(), order.getPurchaseQuantity(), giftQuantity, totalPrice, promotionDiscountedPrice);
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
}
