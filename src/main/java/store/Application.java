package store;

import java.util.List;
import store.controller.InputHandler;
import store.controller.StorageInitializer;
import store.controller.StoreController;
import store.domain.Inventory;
import store.domain.order.Order;
import store.service.OrderService;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args){

        InputView inputView = new InputView();
        OutputView outputView = new OutputView();

        StorageInitializer initializer = new StorageInitializer("products.md", "promotions.md");
        Inventory inventory = initializer.initializeStorage();

        InputHandler inputHandler = new InputHandler(inputView, inventory);

        List<Order> orders = inputHandler.getOrders();
        OrderService orderService = new OrderService(orders, inputHandler);

        StoreController controller = new StoreController(inputHandler, outputView, orderService);


        controller.run();

    }

}
