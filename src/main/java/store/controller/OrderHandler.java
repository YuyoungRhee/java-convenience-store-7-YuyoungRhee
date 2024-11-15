package store.controller;

import java.util.ArrayList;
import java.util.List;
import store.domain.Inventory;
import store.domain.order.Order;
import store.domain.order.OrderValidator;
import store.dto.OrderCheckDto;
import store.dto.OrderRequestDto;
import store.dto.OrderResultDto;
import store.domain.order.OrderFactory;
import store.service.OrderProcessor;

public class OrderHandler {
    private final InputHandler inputHandler;
    private final OrderProcessor orderProcessor;
    private final OrderValidator orderValidator;

    public OrderHandler(InputHandler inputHandler, Inventory inventory, OrderProcessor orderProcessor) {
        this.inputHandler = inputHandler;
        this.orderProcessor = orderProcessor;
        this.orderValidator = new OrderValidator(inventory);
    }

    public List<OrderResultDto> processOrders() {
        List<OrderResultDto> orderResults = new ArrayList<>();

        while (true) {
            try {
                List<OrderRequestDto> orderRequests = inputHandler.getOrderRequests();
                List<Order> orders = OrderFactory.createOrders(orderRequests, orderValidator);

                for (Order order : orders) {
                    orderResults.add(checkAndProcessSingleOrder(order));
                }
                return orderResults;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private OrderResultDto checkAndProcessSingleOrder(Order order) {
        while (true) {
            OrderCheckDto orderCheckDto = order.validateOrder();

            if (!order.isAdditionalInputRequired()) {
                return orderProcessor.processOrder(order);
            }
            requestAdditionalInput(orderCheckDto, order);
        }
    }

    private void requestAdditionalInput(OrderCheckDto orderCheckDto, Order order) {
        handleAvailableGiftQuantity(orderCheckDto, order);
        handleNoPromotionQuantity(orderCheckDto, order);
    }

    private void handleAvailableGiftQuantity(OrderCheckDto orderCheckDto, Order order) {
        boolean addGift = false;
        if (orderCheckDto.getAvailableGiftQuantity() > 0) {
            addGift = inputHandler.askForAdditionalGift(orderCheckDto.getProductName(),
                    orderCheckDto.getAvailableGiftQuantity());
        }
        order.applyAvailableGift(orderCheckDto.getAvailableGiftQuantity(), addGift);

    }

    private void handleNoPromotionQuantity(OrderCheckDto orderCheckDto, Order order) {
        boolean confirmRegularPrice = true;
        if (orderCheckDto.getNoPromotionQuantity() > 0) {
            confirmRegularPrice = inputHandler.askForNoPromotion(orderCheckDto.getProductName(),
                    orderCheckDto.getNoPromotionQuantity());
        }
        order.applyNoPromotionQuantity(orderCheckDto.getNoPromotionQuantity(), confirmRegularPrice);
    }
}