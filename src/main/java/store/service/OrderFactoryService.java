package store.service;

import java.util.ArrayList;
import java.util.List;
import store.controller.InputHandler;
import store.domain.Inventory;
import store.domain.order.Order;
import store.dto.OrderRequestDto;

public class OrderFactoryService {
    private final Inventory inventory;
    private final InputHandler inputHandler;

    public OrderFactoryService(Inventory inventory, InputHandler inputHandler) {
        this.inventory = inventory;
        this.inputHandler = inputHandler;
    }

    public List<Order> createOrders() {
        List<OrderRequestDto> orderRequestDtos = inputHandler.getOrderRequests();
        List<Order> orders = new ArrayList<>();

        for (OrderRequestDto request : orderRequestDtos) {
            Order order = new Order(request.getProductName(), request.getQuantity(), inventory);
            orders.add(order);
        }
        return orders;
    }
}
