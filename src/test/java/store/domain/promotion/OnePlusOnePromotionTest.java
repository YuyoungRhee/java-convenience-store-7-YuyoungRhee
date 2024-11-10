package store.domain.promotion;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.domain.promotionCondition.PromotionCondition;

class OnePlusOnePromotionTest {
    private Promotion promotion;

    @BeforeEach
    void setUp() {
        PromotionCondition promotionCondition = new PromotionCondition() {
            @Override
            public boolean isSatisfiedBy() {
                return true;
            }
        };
        promotion = new Promotion(1,1, promotionCondition);
    }


    @DisplayName("1+1에 맞춰 증정품 개수를 계산한다.")
    @ParameterizedTest
    @CsvSource({
            "1, 0",
            "2, 1",
            "3, 1",
            "4, 2"
    })
    void calculateGiftQuantity(int quantity, int expected) {

        // when
        int giftQuantity = promotion.calculateGiftQuantity(quantity);

        // then
        assertThat(giftQuantity).isEqualTo(expected);
    }

    @DisplayName("최종 결제 수량을 계산한다.")
    @ParameterizedTest
    @CsvSource({
            "1, 1",
            "2, 1",
            "3, 2",
            "4, 2"
    })
    void calculatePayableQuantity(int quantity, int expected) {

        // when
        int giftQuantity = promotion.calculateGiftQuantity(quantity);
        int payableQuantity = quantity - giftQuantity;


        // then
        assertThat(payableQuantity).isEqualTo(expected);
    }

    @DisplayName("할인에 제외되는 수량을 구한다. (1+1이므로 홀수일 때 1, 짝수일때 0)")
    @ParameterizedTest
    @CsvSource({
            "0, 0",
            "1, 1",
            "2, 0",
            "3, 1",
            "4, 0"
    })
    void excludeDiscountQuantity(int quantity, int expected) {
        // when
        int excluded = promotion.excludeDiscountQuantity(quantity);

        // then
        Assertions.assertThat(excluded).isEqualTo(expected);
    }

}