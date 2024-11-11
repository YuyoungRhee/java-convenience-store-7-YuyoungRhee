package store.service;

import java.util.ArrayList;
import java.util.List;
import store.controller.InputHandler;
import store.domain.Order;
import store.dto.OrderCheckDto;
import store.dto.OrderRequestDto;
import store.dto.OrderResultDto;

public class OrderService {
    private final InputHandler inputHandler;
    private final OrderFactoryService orderFactoryService;

    public OrderService(InputHandler inputHandler, OrderFactoryService orderFactoryService) {
        this.inputHandler = inputHandler;
        this.orderFactoryService = orderFactoryService;
    }

    public List<OrderResultDto> processOrders() {
        List<OrderResultDto> orderResults = new ArrayList<>();

        while (true) {
            try {
                List<OrderRequestDto> orderRequests = inputHandler.getOrderRequests();
                List<Order> orders = orderFactoryService.createOrders(orderRequests);

                for (Order order : orders) {
                    OrderResultDto orderResult = checkAndProcessSingleOrder(order);
                    orderResults.add(orderResult);
                }
                return orderResults;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private OrderResultDto checkAndProcessSingleOrder(Order order) {
        while (!order.canProceedOrder()) {
            requestAdditionalInput(order);
        }
        return order.processOrder();
    }

    private void requestAdditionalInput(Order order) {
        OrderCheckDto orderCheckDto = order.checkOrder();

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
    }

    private void handleNoPromotionQuantity(OrderCheckDto orderCheckDto, Order order) {
        if (orderCheckDto.getNoPromotionQuantity() > 0) {
            boolean confirmRegularPrice = inputHandler.askForNoPromotion(orderCheckDto.getProductName(),
                    orderCheckDto.getNoPromotionQuantity());

            if (!confirmRegularPrice) {
                order.excludeNoPromotionQuantity(orderCheckDto.getNoPromotionQuantity());
            }
            order.confirmNoPromotion();
        }
    }
}