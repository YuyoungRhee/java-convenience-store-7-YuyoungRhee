package store;

import store.controller.InputHandler;
import store.controller.StorageInitializer;
import store.controller.StoreController;
import store.domain.Inventory;
import store.service.OrderFactoryService;
import store.service.OrderService;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args){

        InputView inputView = new InputView();
        OutputView outputView = new OutputView();

        StorageInitializer initializer = new StorageInitializer("products.md", "promotions.md");
        Inventory inventory = initializer.initializeStorage();

        InputHandler inputHandler = new InputHandler(inputView);

        OrderFactoryService orderFactoryService = new OrderFactoryService(inventory, inputHandler);
        OrderService orderService = new OrderService(inputHandler, orderFactoryService);

        StoreController controller = new StoreController(inputHandler, outputView, inventory, orderService);

        controller.run();

    }

}
