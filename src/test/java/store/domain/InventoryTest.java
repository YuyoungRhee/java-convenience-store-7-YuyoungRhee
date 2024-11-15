package store.domain;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import store.domain.promotion.NoPromotion;
import store.domain.promotion.Promotion;
import store.domain.promotionCondition.PromotionCondition;
import store.dto.OrderResultDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        PromotionCondition promotionCondition = () -> true;
        Product promotionProduct = new Product("콜라", 1000, 10, new Promotion("1+1프로모",1,1,promotionCondition));
        Product generalProduct = new Product("콜라", 1000, 10, new NoPromotion());
        List<Product> products = List.of(promotionProduct, generalProduct);

        Map<String, List<Product>> initialStock = new HashMap<>();
        initialStock.put("콜라", products);
        inventory = new Inventory(initialStock);
    }

    @Test
    @DisplayName("프로모션 재고와 일반재고를 합친 총 재고를 계산한다.")
    void getAvailableStock() {
        int totalStock = inventory.getAvailableStock("콜라");

        assertThat(totalStock).isEqualTo(20);
    }

    @DisplayName("존재하지 않는 상품 주문 시 예외가 발생한다")
    @Test
    void testProcessOrder_NonExistingProduct() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            inventory.getAvailableStock("사이다");
        });

        assertThat(exception.getMessage()).isEqualTo("[ERROR] 존재하지 않는 상품입니다: 다시 입력해주세요.");
    }





}