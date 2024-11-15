//package store.domain;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import store.domain.discountPolicy.DiscountPolicy;
//import store.domain.discountPolicy.MembershipDiscountPolicy;
//import store.dto.OrderResultDto;
//import store.dto.Receipt;
//
//class SellerTest {
//    private List<OrderResultDto> orderResults;
//
//    @BeforeEach
//    void setUp() {
//        orderResults = new ArrayList<>();
//
//        orderResults.add(new OrderResultDto("콜라", 5, 0, 5000, 0));
//        orderResults.add(new OrderResultDto("사이다", 5, 0, 7000, 2000));
//    }
//
//    @DisplayName("프로모션 적용 후 남은 금액에 대해 멤버십 할인을 적용한다.")
//    @Test
//    void processPayment_withDiscount() {
//        // given
//        DiscountPolicy discountPolicy = new MembershipDiscountPolicy();
//        Seller seller = new Seller(discountPolicy);
//        boolean applyDiscount = true;
//
//        // when
//        Receipt receipt = seller.processPayment(orderResults, applyDiscount);
//
//        // then
//        assertThat(receipt.getFinalAmount()).isEqualTo(7000);
//    }
//
//    @DisplayName("할인을 적용하지 않으면 프로모션 할인만 적용된 금액이 최종 금액이 된다.")
//    @Test
//    void processPayment_AfterPromotionDiscount() {
//        // given
//        DiscountPolicy discountPolicy = new MembershipDiscountPolicy();
//        Seller seller = new Seller(discountPolicy);
//        boolean applyDiscount = false;
//
//        // when
//        Receipt receipt = seller.processPayment(orderResults, applyDiscount);
//
//        // then
//        assertThat(receipt.getFinalAmount()).isEqualTo(10000);
//    }
//
//
//}