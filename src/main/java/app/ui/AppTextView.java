package app.ui;

import app.controllers.AppController;
import app.controllers.ControllerStore;
import app.ui.interfaces.IAppView;
import app.utils.ConsoleReader;
import services.ManagerStore;

public class AppTextView implements IAppView {

    private final AppController appController;
    private final ControllerStore controllerStore = new ControllerStore();

    public AppTextView() {
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
        new CarOrderDetailsTextView(controllerStore.getCarOrderDetailsController());
    }

    @Override
    public void showManager() {
        new ManagerTextView(controllerStore.getManagerController());
    }
}
