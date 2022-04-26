package app.ui;

import app.controllers.AppController;
import app.controllers.ControllerStore;
import app.ui.interfaces.IAppView;
import app.utils.ConsoleReader;

import java.util.List;

public class AppTextView implements IAppView {

    private final AppController appController;
    private final ControllerStore controllerStore;

    public AppTextView() {
        this(new ControllerStore());
    }

    /**
     * Allows passing a controller store with seeded data for running tests
     *
     * @param controllerStore the controller store to use for the application
     */
    public AppTextView(ControllerStore controllerStore) {
        this.controllerStore = controllerStore;
        appController = new AppController(this);
    }

    @Override
    public String showMenu() {
        // is he a mechanic, manager or garage holder?
        return ConsoleReader.getInstance().ask("Who are you? [manager] | [garage holder] | [mechanic] | [quit]");
    }

    @Override
    public void start() {
        appController.initialize();
    }

    @Override
    public void showMechanic() {
        new CarMechanicTextView(controllerStore.getMechanicController());
    }

    @Override
    public void showGarageHolder() {
        var response = ConsoleReader.getInstance().ask("[details] | [order]: ");
        while (!List.of("details", "order").contains(response)) {
            response = ConsoleReader.getInstance().ask("Try again: ");
        }
        if (response.equals("details")) {
            new CheckOrderDetailsTextView(controllerStore.getCheckOrderDetailsController());
        } else {
            new OrderNewCarTextView(controllerStore.getOrderNewCarController());
        }
    }

    @Override
    public void showManager() {
        new ManagerTextView(controllerStore.getManagerController());
    }
}
