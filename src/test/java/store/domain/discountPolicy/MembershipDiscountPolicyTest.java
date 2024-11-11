package store.domain.discountPolicy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class MembershipDiscountPolicyTest {

    private final MembershipDiscountPolicy discountPolicy = new MembershipDiscountPolicy();

    @DisplayName("할인 금액 상한(8000)을 초과하는 경우 최대 할인 금액을 적용한다.")
    @Test
    void applyDiscount_whenDiscountExceedsMax() {
        // given
        int originalPrice = 40000;

        // when
        int discountedPrice = discountPolicy.applyDiscount(originalPrice);

        // then
        assertThat(discountedPrice).isEqualTo(8000);
    }

    @DisplayName("할인 금액 상한(8000) 이하인 경우 할인율(30%)에 맞는 금액을 적용한다.")
    @Test
    void applyDiscount_whenDiscountBelowMax() {
        // given
        int originalPrice = 20000;

        // when
        int discountedPrice = discountPolicy.applyDiscount(originalPrice);

        // then
        assertThat(discountedPrice).isEqualTo(6000); // 할인율 적용 금액
    }
}