package store.dto;

public class OrderCheckDto {
    private final String productName;
    private final int availableGiftQuantity;
    private final int noPromotionQuantity;

    public OrderCheckDto(String productName,  int availableGiftQuantity,int noPromotionQuantity) {
        this.productName = productName;
        this.availableGiftQuantity = availableGiftQuantity;
        this.noPromotionQuantity = noPromotionQuantity;
    }

    public boolean canProceedOrder() {
        return availableGiftQuantity == 0 && noPromotionQuantity == 0;
    }

    public String getProductName() {
        return productName;
    }

    public int getAvailableGiftQuantity() {
        return availableGiftQuantity;
    }

    public int getNoPromotionQuantity() {
        return noPromotionQuantity;
    }
}