package store.controller;

import java.util.ArrayList;
import java.util.List;
import store.domain.Inventory;
import store.domain.Order;
import store.domain.OrderValidator;
import store.dto.OrderCheckDto;
import store.dto.OrderRequestDto;
import store.dto.OrderResultDto;
import store.service.OrderFactoryService;

public class OrderHandler {
    private final InputHandler inputHandler;
    private final OrderFactoryService orderFactoryService;
    private final OrderValidator orderValidator;
    private final Inventory inventory;

    public OrderHandler(InputHandler inputHandler, OrderFactoryService orderFactoryService,
                        OrderValidator orderValidator, Inventory inventory) {
        this.inputHandler = inputHandler;
        this.orderFactoryService = orderFactoryService;
        this.orderValidator = orderValidator;
        this.inventory = inventory;
    }

    public List<OrderResultDto> processOrders() {
        List<OrderResultDto> orderResults = new ArrayList<>();

        while (true) {
            try {
                List<OrderRequestDto> orderRequests = inputHandler.getOrderRequests();
                List<Order> orders = orderFactoryService.createOrders(orderRequests);

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

            if (canProceedOrder(orderCheckDto, order)) {
                return inventory.processOrder(order.getProductName(), order.getPurchaseQuantity());
            } else {
                requestAdditionalInput(orderCheckDto, order);
            }
        }
    }

    private boolean canProceedOrder(OrderCheckDto orderCheckDto, Order order) {
        return orderCheckDto.isEnough() &&
                order.isConfirmAvailableGift() && order.isConfirmNoPromotion();
    }

    private void requestAdditionalInput(OrderCheckDto orderCheckDto, Order order) {
        if (!orderCheckDto.isEnough()) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }

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