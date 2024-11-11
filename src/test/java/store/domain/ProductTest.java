package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.promotion.Promotion;
import store.domain.promotionCondition.PromotionCondition;

class ProductTest {

    @DisplayName("재고 수량을 요구하는 수량만큼 차감한다.")
    @Test
    void reduceQuantity() {
        // given
        int stockQuantity = 10;
        int requiredQuantity = 1;
        PromotionCondition promotionCondition = () -> true;

        Promotion promotion = new Promotion("1+1프로모션", 1,1, promotionCondition);
        Product product = new Product("Test product", 1000, stockQuantity, promotion);

        // when
        product.reduceQuantity(requiredQuantity);

        // then
        assertThat(product.getQuantity()).isEqualTo(9);
    }

    @DisplayName("할인되는 금액을 계산한다.")
    @Test
    void calculateDiscountedPrice() {
        // given
        int stockQuantity = 10;
        int requiredQuantity = 10;
        PromotionCondition promotionCondition = () -> true;

        Promotion promotion = new Promotion("1+1프로모션", 1,1, promotionCondition);
        Product product = new Product("Test product", 1000, stockQuantity, promotion);

        // when
        int discountedPrice = product.calculateDiscountedPrice(requiredQuantity);

        // then
        assertThat(discountedPrice).isEqualTo(5000);
    }

}