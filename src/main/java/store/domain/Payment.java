package store.domain;

import java.util.List;
import store.dto.OrderResultDto;
import store.dto.Receipt;

public class Payment {

    private final Seller seller;

    public Payment(Seller seller) {
        this.seller = seller;
    }

    public Receipt processPayment(List<OrderResultDto> orderResults, boolean applyDiscount) {
        int finalAmount = seller.calculateFinalAmount(orderResults, applyDiscount);
        int totalPrice = seller.calculateTotalPrice(orderResults);
        int totalDiscount = seller.calculateTotalDiscount(orderResults);
        int discountAmount = totalPrice - totalDiscount - finalAmount;

        return new Receipt(totalPrice, totalDiscount, discountAmount, finalAmount, orderResults);
    }
}