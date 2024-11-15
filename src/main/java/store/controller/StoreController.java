package store.controller;

import java.util.List;
import store.domain.Inventory;
import store.domain.Payment;
import store.dto.InventoryDto;
import store.dto.OrderResultDto;
import store.dto.Receipt;
import store.domain.Seller;
import store.domain.discountPolicy.DiscountPolicy;
import store.domain.discountPolicy.MembershipDiscountPolicy;
import store.view.OutputView;

public class StoreController {
    private final Payment payment;
    private final InputHandler inputHandler;
    private final OrderHandler orderHandler;
    private final OutputView outputView;
    private final Inventory inventory;

    public StoreController(InputHandler inputHandler, OutputView outputView, Inventory inventory, OrderHandler orderHandler) {
        this.outputView = outputView;
        this.inputHandler = inputHandler;
        this.orderHandler = orderHandler;
        this.inventory = inventory;
        this.payment = initializePayment();
    }

    public void run() {
        do {
            displayStartMessage();

            List<OrderResultDto> orderResults = orderHandler.processOrders();

            boolean applyDiscount = inputHandler.askApplyDiscount();
            Receipt receipt = payment.processPayment(orderResults, applyDiscount);
            outputView.printReceipt(receipt);

        } while (wantMorePurchase());
    }

    private void displayStartMessage() {
        outputView.printWelcomeMessage();
        outputView.printProducts(InventoryDto.from(inventory));
        System.out.flush();
    }

    private boolean wantMorePurchase() {
        return inputHandler.askForAdditionalPurchase();
    }

    private Payment initializePayment() {
        DiscountPolicy discountPolicy = new MembershipDiscountPolicy();
        Seller seller = new Seller(discountPolicy);
        return new Payment(seller);
    }

}
