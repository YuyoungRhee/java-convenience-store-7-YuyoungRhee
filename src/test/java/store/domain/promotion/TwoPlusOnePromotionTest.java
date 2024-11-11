package store.domain.promotion;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.domain.Product;
import store.domain.promotionCondition.PromotionCondition;

class TwoPlusOnePromotionTest {
    private Promotion promotion;

    @BeforeEach
    void setUp() {
        PromotionCondition promotionCondition = () -> true;
        promotion = new Promotion("2+1프로모션",2,1, promotionCondition);
    }

    @DisplayName("2+1에 맞춰 증정품 개수를 계산한다.")
    @ParameterizedTest
    @CsvSource({
            "2, 0",
            "3, 1",
            "4, 1",
            "6, 2",
            "9, 3"
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
            "2, 2",
            "3, 2",
            "4, 3",
            "6, 4",
            "9, 6"
    })
    void calculatePayableQuantity(int quantity, int expected) {
        // when
        int giftQuantity = promotion.calculateGiftQuantity(quantity);
        int payableQuantity = quantity - giftQuantity;

        // then
        assertThat(payableQuantity).isEqualTo(expected);
    }

    @DisplayName("무료로 증정 가능한 개수를 계산한다.")
    @ParameterizedTest
    @CsvSource({
            "1, 0",
            "2, 1",
            "3, 0",
            "4, 0",
            "5, 1",
            "8, 1",
    })
    void calculateAvailableGiftQuantity(int quantity, int expected) {
        // when
        int additionalGift = promotion.calculateAvailableGiftQuantity(quantity);

        // then
        assertThat(additionalGift).isEqualTo(expected);
    }

    @DisplayName("할인에 제외되는 수량을 구한다.")
    @ParameterizedTest
    @CsvSource({
            "3, 0",
            "4, 1",
            "5, 2",
            "6, 0",
            "10, 1"
    })
    void excludeDiscountQuantity(int quantity, int expected) {
        // when
        int excluded = promotion.excludeDiscountQuantity(quantity);

        // then
        Assertions.assertThat(excluded).isEqualTo(expected);
    }

}