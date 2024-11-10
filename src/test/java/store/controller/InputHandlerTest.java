package store.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Inventory;
import store.domain.Product;
import store.domain.order.Order;
import store.domain.order.OrderFactory;
import store.domain.promotion.NoPromotion;
import store.domain.promotionCondition.PromotionCondition;
import store.view.InputView;

class InputHandlerTest {
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        Product coke = new Product("사이다", 1000, 10, new NoPromotion());
        Product potato = new Product("감자칩", 1000, 10, new NoPromotion());
        List<Product> products = List.of(coke, potato);

        Map<String, List<Product>> initialStock = new HashMap<>();
        initialStock.put("콜라", products);
        inventory = new Inventory(initialStock);
    }

    @DisplayName("주문 문자열을 처리하여 주문 리스트로 반환")
    @Test
    void getOrders() {
        // given
        InputView inputView = new InputView();
        OrderFactory orderFactory = new OrderFactory(inventory);
        InputHandler inputHandler = new InputHandler(inputView, orderFactory);

        String input = "[사이다-2],[감자칩-1]";

        // when
        List<Order> orders = inputHandler.getOrders();

        // then
        Assertions.assertThat(orders).hasSize(2);
    }

}