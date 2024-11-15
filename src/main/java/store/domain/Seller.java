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

    public int calculateFinalAmount(List<OrderResultDto> orderResults, boolean applyDiscount) {
        int totalPrice = calculateTotalPrice(orderResults);
        int discountPrice = calculateTotalDiscount(orderResults);

        if (applyDiscount) {
            int finalDiscount = discountPolicy.applyDiscount(totalPrice - discountPrice);
            return totalPrice - discountPrice - finalDiscount;
        }
        return totalPrice - discountPrice;
    }

    public int calculateTotalPrice(List<OrderResultDto> orderResults) {
        return orderResults.stream()
                .mapToInt(OrderResultDto::getTotalPrice)
                .sum();
    }

    public int calculateTotalDiscount(List<OrderResultDto> orderResults) {
        return orderResults.stream()
                .mapToInt(OrderResultDto::getDiscountedPrice)
                .sum();
    }
}
