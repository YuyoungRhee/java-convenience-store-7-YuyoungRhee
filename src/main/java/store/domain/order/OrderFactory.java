package store.domain.order;

import java.util.ArrayList;
import java.util.List;
import store.dto.OrderRequestDto;

public class OrderFactory {

    public static List<Order> createOrders(List<OrderRequestDto> orderRequestDtos, OrderValidator orderValidator) {
        List<Order> orders = new ArrayList<>();

        for (OrderRequestDto request : orderRequestDtos) {
            Order order = new Order(
                    request.getProductName(),
                    request.getQuantity(),
                    orderValidator
            );
            orders.add(order);
        }
        return orders;
    }
}
