package store.controller;

import java.util.List;
import store.dto.OrderRequestDto;

public class InputValidator {

    public static void validateOrderInput(String input) {
        validateNullOrBlank(input);
        validateFormat(input);
    }

    public static void validateOrderItems(List<OrderRequestDto> orderRequestDtos) {
        for (OrderRequestDto dto : orderRequestDtos) {
            validateNullOrBlank(dto.getProductName());
            validateQuantity(dto.getQuantity());
        }
    }

    private static void validateNullOrBlank(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 입력값이 비어 있습니다. 올바르게 입력해 주세요.");
        }
    }

    private static void validateFormat(String input) {
        if (!input.matches("\\[.*\\-\\d+\\](,\\[.*\\-\\d+\\])*")) {
            throw new IllegalArgumentException("[ERROR] 입력 형식이 잘못되었습니다. 예: [사이다-2],[감자칩-1]");
        }
    }

    private static void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("[ERROR] 수량은 1 이상이어야 합니다.");
        }
    }
}
