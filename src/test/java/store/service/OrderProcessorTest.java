//package store.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import store.dto.OrderResultDto;
//
//class OrderProcessorTest {
//
//    //주문진행테스트
//    @DisplayName("주문한 상품명과 수량이 결과로 반환된다.")
//    @Test
//    void processOrder() {
//        //given
//        int requestedQuantity = 15;
//
//        //when
//        OrderResultDto orderResult = orderProcessor.processOrder("콜라", requestedQuantity);
//
//        // then
//        assertThat(orderResult.getPruductName()).isEqualTo("콜라");
//        assertThat(orderResult.getPurchaseQuantity()).isEqualTo(15);
//    }
//
//    @DisplayName("증정 수량과 행사 할인 금액 결과가 반환된다.")
//    @Test
//    void processOrder_PromotionDiscounted() {
//        //given
//        int requestedQuantity = 15;
//
//        //when
//        OrderResultDto orderResult = orderProcessor.processOrder("콜라", requestedQuantity);
//
//        // then
//        assertThat(orderResult.getGiftQuantity()).isEqualTo(5);
//        assertThat(orderResult.getDiscountedPrice()).isEqualTo(5000);
//    }
//
//}