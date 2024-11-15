package store;

import store.controller.InputHandler;
import store.controller.OrderHandler;
import store.controller.StorageInitializer;
import store.controller.StoreController;
import store.domain.Inventory;
import store.domain.OrderValidator;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args){

        InputView inputView = new InputView();
        OutputView outputView = new OutputView();

        StorageInitializer initializer = new StorageInitializer("products.md", "promotions.md");
        Inventory inventory = initializer.initializeStorage();

        InputHandler inputHandler = new InputHandler(inputView);
        OrderValidator orderValidator = new OrderValidator(inventory);
        OrderHandler orderHandler = new OrderHandler(inputHandler, orderValidator, inventory);

        StoreController controller = new StoreController(inputHandler, outputView, inventory, orderHandler);

        controller.run();

    }

}
