package store.domain;

import store.dto.OrderCheckDto;

public class OrderValidator {
    private final Inventory inventory;

    public OrderValidator(Inventory inventory) {
        this.inventory = inventory;
    }

    public OrderCheckDto checkOrder(String productName, int requestedQuantity) {
        int totalStock = inventory.getAvailableStock(productName);
        boolean isEnough = totalStock >= requestedQuantity;

        int availableGiftQuantity = calculateAvailableGiftQuantity(productName, requestedQuantity);
        int promotionQuantity = calculatePromotionQuantity(productName, requestedQuantity);
        int noPromotionQuantity = calculateNoPromotionQuantity(productName, requestedQuantity, promotionQuantity);

        if (promotionQuantity == 0) {
            noPromotionQuantity = 0;
        }

        return new OrderCheckDto(productName, isEnough, availableGiftQuantity, noPromotionQuantity);
    }

    private int calculateAvailableGiftQuantity(String productName, int requestedQuantity) {
        int availableGiftQuantity = 0;
        int remainingQuantity = requestedQuantity;

        for (Product product : inventory.getProducts(productName)) {
            int usedQuantity = Math.min(remainingQuantity, product.getQuantity());
            availableGiftQuantity += product.calculateAdditionalGiftQuantity(usedQuantity);
            remainingQuantity -= usedQuantity;
            if (remainingQuantity <= 0) break;
        }
        return availableGiftQuantity;
    }

    private int calculatePromotionQuantity(String productName, int requestedQuantity) {
        int promotionQuantity = 0;
        int remainingQuantity = requestedQuantity;

        for (Product product : inventory.getProducts(productName)) {
            int usedQuantity = Math.min(remainingQuantity, product.getQuantity());
            promotionQuantity += product.getPromotionQuantity(remainingQuantity);
            remainingQuantity -= usedQuantity;
            if (remainingQuantity <= 0) break;
        }
        return promotionQuantity;
    }

    private int calculateNoPromotionQuantity(String productName, int requestedQuantity, int promotionQuantity) {
        int noPromotionQuantity = 0;

        for (Product product : inventory.getProducts(productName)) {
            int applicableQuantity = Math.min(requestedQuantity, product.getQuantity());
            noPromotionQuantity += product.excludeDiscountQuantity(applicableQuantity);
            requestedQuantity -= applicableQuantity;

            if (requestedQuantity <= 0) break;
        }

        return noPromotionQuantity;
    }
}
