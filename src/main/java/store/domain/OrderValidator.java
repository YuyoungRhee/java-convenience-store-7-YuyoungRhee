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

        for(Product product : inventory.getProducts(productName)) {
            if(product.isPromotionProduct()) {
                promotionQuantity += product.getQuantity();
            }
        }
        return Math.min(promotionQuantity, requestedQuantity);
    }

    private int calculateNoPromotionQuantity(String productName, int requestedQuantity, int promotionQuantity) {
        if(requestedQuantity <= promotionQuantity) {
            return 0;
        }

        int usedPromotionQuantity = 0;
        for (Product product : inventory.getProducts(productName)) {
            usedPromotionQuantity += product.getPromotionQuantity(requestedQuantity);
        }

        return  requestedQuantity - usedPromotionQuantity;
    }
}
