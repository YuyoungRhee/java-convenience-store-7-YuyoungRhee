package store.controller;

import java.util.List;
import store.domain.Inventory;
import store.dto.OrderResultDto;
import store.dto.Receipt;
import store.domain.Seller;
import store.domain.discountPolicy.DiscountPolicy;
import store.domain.discountPolicy.MembershipDiscountPolicy;
import store.view.OutputView;

public class StoreController {
    private final Seller seller;
    private final InputHandler inputHandler;
    private final OrderHandler orderHandler;
    private final OutputView outputView;
    private final Inventory inventory;

    public StoreController(InputHandler inputHandler, OutputView outputView, Inventory inventory, OrderHandler orderHandler) {
        this.outputView = outputView;
        this.inputHandler = inputHandler;
        this.orderHandler = orderHandler;
        this.inventory = inventory;
        this.seller = initializeSeller();
    }

    public void run() {
        do {
            displayStartMessage();

            List<OrderResultDto> orderResults = orderHandler.processOrders();

            boolean applyDiscount = inputHandler.askApplyDiscount();
            Receipt receipt = seller.processPayment(orderResults, applyDiscount);
            outputView.printReceipt(receipt);

        } while (wantMorePurchase());
    }

    private void displayStartMessage() {
        outputView.printWelcomeMessage();
        outputView.printProducts(inventory.toDto());
        System.out.flush();
    }

    private boolean wantMorePurchase() {
        return inputHandler.askForAdditionalPurchase();
    }

    private Seller initializeSeller() {
        DiscountPolicy discountPolicy = new MembershipDiscountPolicy();
        return new Seller(discountPolicy);
    }

}
