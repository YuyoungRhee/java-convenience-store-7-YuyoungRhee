package store.domain;

import java.util.List;
import store.dto.Receipt;
import store.domain.discountPolicy.DiscountPolicy;
import store.dto.OrderResultDto;

public class Seller {
    private final DiscountPolicy discountPolicy;

    public Seller(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public Receipt processPayment(List<OrderResultDto> orderResults, boolean applyDiscount) {

        int totalPrice = orderResults.stream()
                .mapToInt(OrderResultDto::getTotalPrice)
                .sum();

        int discountPrice = orderResults.stream()
                .mapToInt(OrderResultDto::getDiscountedPrice)
                .sum();

        int finalDiscountPrice = 0;
        if (applyDiscount) {
            finalDiscountPrice = discountPolicy.applyDiscount(totalPrice - discountPrice);
        }
        int finalAmount = totalPrice - discountPrice - finalDiscountPrice;

        return new Receipt(totalPrice, discountPrice, finalDiscountPrice, finalAmount, orderResults);
    }
}
