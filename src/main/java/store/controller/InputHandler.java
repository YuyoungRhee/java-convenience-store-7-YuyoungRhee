package store.controller;

import java.util.ArrayList;
import java.util.List;
import store.dto.OrderRequestDto;
import store.view.InputView;

public class InputHandler {
    private final InputView inputView;

    public InputHandler(InputView inputView) {
        this.inputView = inputView;
    }

    public List<OrderRequestDto> getOrderRequests() {
        String orderInput = inputView.readOrders();
        //검증로직 추가 필요 예외처리도
        List<OrderRequestDto> orderRequestDtos = new ArrayList<>();
        String[] orderItems = orderInput.split(",");

        for (String item : orderItems) {
            item = item.trim();
            String[] parts = item.split("-");
            if (parts.length == 2) {
                String productName = parts[0].substring(1); // 대괄호 제거
                int quantity = Integer.parseInt(parts[1].substring(0, parts[1].length() - 1)); // 수량 추출
                System.out.println("productName = " + productName);
                orderRequestDtos.add(new OrderRequestDto(productName, quantity));
            }
        }
        return orderRequestDtos;
    }
    public boolean askForNoPromotion(String productName, int noPromotionQuantity) {
        String prompt = String.format(
                "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)",
                productName, noPromotionQuantity
        );
        String input = inputView.readAnswerForNoPromotion(prompt);
        //검증로직

        return input.equals("Y");

    }
    public boolean askForAdditionalGift(String productName, int additionalGift) {
        String prompt = String.format(
                "현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)",
                productName, additionalGift
        );
        String input = inputView.readAnswerForAdditionalGift(prompt);
        //검증로직

        return input.equals("Y");
    }

    public boolean askApplyDiscount() {
        String input = inputView.readAnswerForApplyDiscount();
        //검증 로직

        return input.equals("Y");
    }

    public boolean askForAdditionalPurchase() {
        String input = inputView.readAnswerForAdditionalPurchase();
        //검증 로직

        return input.equals("Y");
    }
}
