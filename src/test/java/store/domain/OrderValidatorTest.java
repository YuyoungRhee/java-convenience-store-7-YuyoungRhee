package store.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.promotion.NoPromotion;
import store.domain.promotion.Promotion;
import store.domain.promotionCondition.PromotionCondition;
import store.dto.OrderCheckDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderValidatorTest {
    private OrderValidator orderValidator;

    @BeforeEach
    void setUp() {
        PromotionCondition promotionCondition = () -> true;
        Product promotionProduct = new Product("콜라", 1000, 10, new Promotion(1, 1, promotionCondition));
        Product generalProduct = new Product("콜라", 1000, 10, new NoPromotion());
        List<Product> products = List.of(promotionProduct, generalProduct);

        Map<String, List<Product>> initialStock = new HashMap<>();
        initialStock.put("콜라", products);
        Inventory inventory = new Inventory(initialStock);

        orderValidator = new OrderValidator(inventory);
    }

    @Test
    @DisplayName("재고가 충분할 경우, 주문 확인 결과가 '충분함'으로 반환된다")
    void check_stockEnough() {
        int requestedQuantity = 15;
        OrderCheckDto orderCheckDto = orderValidator.checkOrder("콜라", requestedQuantity);

        assertThat(orderCheckDto.isEnough()).isTrue();
    }

    @Test
    @DisplayName("재고가 부족할 경우, 주문 확인 결과가 '충분하지 않음'으로 반환된다")
    void check_stockNotEnough() {
        int requestedQuantity = 25;
        OrderCheckDto orderCheckDto = orderValidator.checkOrder("콜라", requestedQuantity);

        assertThat(orderCheckDto.isEnough()).isFalse();
    }

    @DisplayName("프로모션 적용이 가능한 상품에 대해 해당 수량만큼 구매하지 않을 경우, 추가 증정이 가능한 수량이 반환된다")
    @Test
    void check_availableGiftQuantity() {
        int requestedQuantity = 5;
        OrderCheckDto orderCheckDto = orderValidator.checkOrder("콜라", requestedQuantity);

        assertThat(orderCheckDto.getAvailableGiftQuantity()).isEqualTo(1);
    }

    @DisplayName("프로모션 재고가 부족하여 일부 수량을 혜택 없이 결제해야 하는 경우, 정가로 결제해야 하는 수량을 반환한다")
    @Test
    void check_noPromotionQuantity() {
        int requestedQuantity = 15;
        OrderCheckDto orderCheckDto = orderValidator.checkOrder("콜라", requestedQuantity);

        assertThat(orderCheckDto.getNoPromotionQuantity()).isEqualTo(5);
    }

    @DisplayName("존재하지 않는 상품 주문 시 예외가 발생한다")
    @Test
    void testCheckOrder_NonExistingProduct() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderValidator.checkOrder("사이다", 1);
        });

        assertThat(exception.getMessage()).isEqualTo("[ERROR] 존재하지 않는 상품입니다: 사이다");
    }
}