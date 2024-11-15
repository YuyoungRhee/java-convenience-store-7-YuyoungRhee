//package store.service;
//
//import java.util.ArrayList;
//import java.util.List;
//import store.controller.InputHandler;
//import store.domain.Inventory;
//import store.domain.Order;
//import store.domain.OrderValidator;
//import store.dto.OrderCheckDto;
//import store.dto.OrderRequestDto;
//import store.dto.OrderResultDto;
//
//public class OrderService {
//    private final Inventory inventory;
//    private final OrderValidator orderValidator;
//
//    public OrderService(Inventory inventory, OrderValidator orderValidator) {
//        this.inventory = inventory;
//        this.orderValidator = orderValidator;
//    }
//
//    public OrderResultDto checkAndProcessSingleOrder(Order order) {
//        while (!canProceedOrder(order)) {
//            requestAdditionalInput(order);
//        }
//        return processOrder(order);
//    }
//
//    private boolean canProceedOrder(Order order) {
//        OrderCheckDto orderCheckDto = checkOrder(order);
//        return orderCheckDto.isEnough() && orderCheckDto.getAvailableGiftQuantity() == 0 &&
//                orderCheckDto.getNoPromotionQuantity() == 0;
//    }
//
//    private OrderCheckDto checkOrder(Order order) {
//        return orderValidator.checkOrder(order.getProductName(), order.getPurchaseQuantity());
//    }
//
//    private OrderResultDto processOrder(Order order) {
//        return inventory.processOrder(order.getProductName(), order.getPurchaseQuantity());
//    }
//}