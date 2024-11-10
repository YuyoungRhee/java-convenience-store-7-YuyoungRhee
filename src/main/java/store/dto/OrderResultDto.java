package store.dto;

public class OrderResultDto {
    private final String pruductName;
    private final int purchaseQuantity;
    private final int giftQuantity;
    private final int totalPrice;
    private final int discountedPrice;


    public OrderResultDto(String pruductName, int purchaseQuantity, int giftQuantity, int totalPrice, int discountedPrice) {
        this.pruductName = pruductName;
        this.purchaseQuantity = purchaseQuantity;
        this.giftQuantity = giftQuantity;
        this.totalPrice = totalPrice;
        this.discountedPrice = discountedPrice;
    }

    public String getPruductName() {
        return pruductName;
    }

    public int getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public int getGiftQuantity() {
        return giftQuantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }
}