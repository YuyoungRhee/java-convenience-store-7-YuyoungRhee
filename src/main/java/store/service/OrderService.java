package store.service;

import java.util.ArrayList;
import java.util.List;
import store.controller.InputHandler;
import store.domain.Order;
import store.dto.OrderCheckDto;
import store.dto.OrderResultDto;

public class OrderService {
    private final List<Order> orders;
    private final InputHandler inputHandler;
    private final OrderFactoryService orderFactoryService;


    public OrderService(InputHandler inputHandler, OrderFactoryService orderFactoryService) {
        this.inputHandler = inputHandler;
        this.orderFactoryService = orderFactoryService;
        this.orders = orderFactoryService.createOrders();
    }

    public List<OrderResultDto> processOrders() {
        List<OrderResultDto> orderResults = new ArrayList<>();
        for (Order order : orders) {
            OrderResultDto orderResult = checkAndProcessSingleOrder(order);
            orderResults.add(orderResult);
        }
        return orderResults;
    }

    private OrderResultDto checkAndProcessSingleOrder(Order order) {
        while(true) {
            OrderCheckDto orderCheckDto = order.checkOrder();

            if (orderCheckDto.canProceedOrder()) {
                break;
            }

            requestAdditionalInput(orderCheckDto, order);
        }
        return order.processOrder();
    }

    private void requestAdditionalInput(OrderCheckDto orderCheckDto, Order order) {
        if (!orderCheckDto.isEnough()) {
            System.out.println("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
            inputHandler.getOrderRequests();
        }

        if (orderCheckDto.getAvailableGiftQuantity() > 0) {
            boolean addGift = inputHandler.askForAdditionalGift(orderCheckDto.getProductName(),
                    orderCheckDto.getAvailableGiftQuantity());
            if (addGift) {
                order.addAvailableGiftQuantity(orderCheckDto.getAvailableGiftQuantity());
            }
        }

        if (orderCheckDto.getNoPromotionQuantity() > 0) {
            boolean confirmRegularPrice = inputHandler.askForNoPromotion(orderCheckDto.getProductName(),
                    orderCheckDto.getNoPromotionQuantity());
            if (confirmRegularPrice) {
                order.excludeNoPromotionQuantity(orderCheckDto.getNoPromotionQuantity());
            }
        }
    }
}