package store.dto;

import java.util.List;

public class Receipt {
    private final int totalPrice;
    private final int discountPrice;
    private final int finalDiscountPrice;
    private final int finalAmount;
    private final List<OrderResultDto> orderResults;

    public Receipt(int totalPrice, int discountPrice, int finalDiscountPrice, int finalAmount,
                   List<OrderResultDto> orderResults) {
        this.totalPrice = totalPrice;
        this.discountPrice = discountPrice;
        this.finalDiscountPrice = finalDiscountPrice;
        this.finalAmount = finalAmount;
        this.orderResults = orderResults;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public int getFinalDiscountPrice() {
        return finalDiscountPrice;
    }

    public int getFinalAmount() {
        return finalAmount;
    }

    public List<OrderResultDto> getOrderResults() {
        return orderResults;
    }
}
