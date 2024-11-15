package store.controller;

import java.util.ArrayList;
import java.util.List;
import store.domain.Inventory;
import store.domain.Order;
import store.domain.OrderValidator;
import store.dto.OrderCheckDto;
import store.dto.OrderRequestDto;
import store.dto.OrderResultDto;
import store.domain.OrderFactory;
import store.service.OrderProcessor;

public class OrderHandler {
    private final InputHandler inputHandler;
    private final OrderValidator orderValidator;
    private final OrderProcessor orderProcessor;

    public OrderHandler(InputHandler inputHandler, OrderValidator orderValidator, OrderProcessor orderProcessor) {
        this.inputHandler = inputHandler;
        this.orderValidator = orderValidator;
        this.orderProcessor = orderProcessor;
    }

    public List<OrderResultDto> processOrders() {
        List<OrderResultDto> orderResults = new ArrayList<>();

        while (true) {
            try {
                List<OrderRequestDto> orderRequests = inputHandler.getOrderRequests();
                List<Order> orders = OrderFactory.createOrders(orderRequests);

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
            OrderCheckDto orderCheckDto = orderValidator.checkOrder(order.getProductName(), order.getPurchaseQuantity());

            if (canProceedOrder(order)) {
                return orderProcessor.processOrder(order);
            }
            requestAdditionalInput(orderCheckDto, order);
        }
    }

    private boolean canProceedOrder(Order order) {
        return order.isConfirmAvailableGift() && order.isConfirmNoPromotion();
    }

    private void requestAdditionalInput(OrderCheckDto orderCheckDto, Order order) {
        handleAvailableGiftQuantity(orderCheckDto, order);
        handleNoPromotionQuantity(orderCheckDto, order);
    }

    private void handleAvailableGiftQuantity(OrderCheckDto orderCheckDto, Order order) {
        if (orderCheckDto.getAvailableGiftQuantity() > 0) {
            boolean addGift = inputHandler.askForAdditionalGift(orderCheckDto.getProductName(),
                    orderCheckDto.getAvailableGiftQuantity());
            if (addGift) {
                order.addAvailableGiftQuantity(orderCheckDto.getAvailableGiftQuantity());
            }
        }
        order.setConfirmAvailableGift();
    }

    private void handleNoPromotionQuantity(OrderCheckDto orderCheckDto, Order order) {
        if (orderCheckDto.getNoPromotionQuantity() > 0) {
            boolean confirmRegularPrice = inputHandler.askForNoPromotion(orderCheckDto.getProductName(),
                    orderCheckDto.getNoPromotionQuantity());

            if (!confirmRegularPrice) {
                order.excludeNoPromotionQuantity(orderCheckDto.getNoPromotionQuantity());
            }
        }
        order.setConfirmNoPromotion();
    }
}