package store.domain;

import java.util.ArrayList;
import java.util.List;
import store.domain.Order;
import store.dto.OrderRequestDto;

public class OrderFactory {

    public static List<Order> createOrders(List<OrderRequestDto> orderRequestDtos) {
        List<Order> orders = new ArrayList<>();

        for (OrderRequestDto request : orderRequestDtos) {
            Order order = new Order(
                    request.getProductName(),
                    request.getQuantity()
            );
            orders.add(order);
        }
        return orders;
    }
}
