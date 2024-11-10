package store.controller;

import java.util.List;
import store.dto.OrderResultDto;
import store.dto.Receipt;
import store.domain.Seller;
import store.domain.discountPolicy.DiscountPolicy;
import store.domain.discountPolicy.MembershipDiscountPolicy;
import store.service.OrderService;
import store.view.OutputView;

public class StoreController {
    private final Seller seller;
    private final InputHandler inputHandler;
    private final OutputView outputView;
    private final OrderService orderService;

    public StoreController(InputHandler inputHandler, OutputView outputView, OrderService orderService) {
        this.outputView = outputView;
        this.inputHandler = inputHandler;
        this.orderService = orderService;
        this.seller = initializeSeller();
    }

    public void run() {
        while (true) {
            outputView.printWelcomeMessage();
            outputView.printProductsFromFile("products.md");

            List<OrderResultDto> orderResults = orderService.processOrders();

            boolean applyDiscount = inputHandler.askApplyDiscount();
            Receipt receipt = seller.processPayment(orderResults, applyDiscount);

            outputView.printReceipt(receipt);
            boolean wantMorePurchase = inputHandler.askForAdditionalPurchase();
            if (!wantMorePurchase) {
                break;
            }
        }

    }

    private Seller initializeSeller() {
        DiscountPolicy discountPolicy = new MembershipDiscountPolicy();
        return new Seller(discountPolicy);
    }

}
