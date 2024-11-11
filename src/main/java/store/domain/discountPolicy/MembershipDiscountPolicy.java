package store.domain.discountPolicy;

public class MembershipDiscountPolicy implements DiscountPolicy{
    private static final int DISCOUNT_RATE = 30;
    private static final int MAX_DISCOUNT_AMOUNT = 8000;

    @Override
    public int applyDiscount(int originalPrice) {
        int discountAmount = (originalPrice * DISCOUNT_RATE) / 100;
        return Math.min(discountAmount, MAX_DISCOUNT_AMOUNT);
    }

}
