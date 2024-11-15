package store;

import store.controller.InputHandler;
import store.controller.OrderHandler;
import store.controller.StorageInitializer;
import store.controller.StoreController;
import store.domain.Inventory;
import store.domain.OrderValidator;
import store.service.PromotionCalculator;
import store.service.OrderProcessor;
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
        PromotionCalculator promotionCalculator = new PromotionCalculator();
        OrderProcessor orderProcessor = new OrderProcessor(inventory, promotionCalculator);
        OrderHandler orderHandler = new OrderHandler(inputHandler, inventory, orderProcessor);

        StoreController controller = new StoreController(inputHandler, outputView, inventory, orderHandler);

        controller.run();

    }

}
