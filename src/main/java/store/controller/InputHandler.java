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
        while (true) {
            try {
                String orderInput = inputView.readOrders();
                InputValidator.validateOrderInput(orderInput);
                List<OrderRequestDto> orderRequestDtos = parseOrderInput(orderInput);
                InputValidator.validateOrderItems(orderRequestDtos);
                return orderRequestDtos;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private List<OrderRequestDto> parseOrderInput(String orderInput) {
        List<OrderRequestDto> orderRequestDtos = new ArrayList<>();
        String[] orderItems = orderInput.split(",");

        for (String item : orderItems) {
            item = item.trim();
            String[] parts = item.substring(1, item.length() - 1).split("-");
            String productName = parts[0].trim();
            int quantity = Integer.parseInt(parts[1].trim());
            orderRequestDtos.add(new OrderRequestDto(productName, quantity));
        }
        return orderRequestDtos;
    }

    public boolean askForNoPromotion(String productName, int noPromotionQuantity) {
        String prompt = String.format(
                "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)",
                productName, noPromotionQuantity
        );
        return askYesOrNo(prompt);
    }

    public boolean askForAdditionalGift(String productName, int additionalGift) {
        String prompt = String.format(
                "현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)",
                productName, additionalGift
        );
        return askYesOrNo(prompt);
    }

    public boolean askApplyDiscount() {
        return askYesOrNo("\n멤버십 할인을 받으시겠습니까? (Y/N)");
    }

    public boolean askForAdditionalPurchase() {
        return askYesOrNo("\n감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
    }

    private boolean askYesOrNo(String prompt) {
        while (true) {
            try {
                String input = inputView.readYesOrNo(prompt);
                InputValidator.validateYesOrNo(input);
                return input.equalsIgnoreCase("Y");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
