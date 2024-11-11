package store.dto;

public class OrderCheckDto {
    private final String productName;
    private final int availableGiftQuantity;
    private final boolean isEnough;
    private final int noPromotionQuantity;

    public OrderCheckDto(String productName,  boolean isEnough, int availableGiftQuantity,int noPromotionQuantity) {
        this.productName = productName;
        this.isEnough = isEnough;
        this.availableGiftQuantity = availableGiftQuantity;
        this.noPromotionQuantity = noPromotionQuantity;
    }

    public boolean canProceedOrder() {
        return isEnough && availableGiftQuantity == 0 && noPromotionQuantity == 0;
    }

    public String getProductName() {
        return productName;
    }

    public int getAvailableGiftQuantity() {
        return availableGiftQuantity;
    }

    public boolean isEnough() {
        return isEnough;
    }

    public int getNoPromotionQuantity() {
        return noPromotionQuantity;
    }
}