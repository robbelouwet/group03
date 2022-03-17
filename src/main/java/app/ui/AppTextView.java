package app.ui;

import app.controllers.AppController;
import app.ui.interfaces.IAppView;
import app.utils.ConsoleReader;
import services.ManagerStore;

public class AppTextView implements IAppView {

    private final AppController appController;

    public AppTextView() {
        appController = new AppController(this);
    }

    @Override
    public String showMenu() {
        // TODO: show the menu
        // is he a mechanic, manager or garage holder?
        return ConsoleReader.getInstance().ask("Who are you? [manager] | [garage holder] | [mechanic] | [quit]");
    }

    @Override
    public void start() {

        appController.initialize();
    }
}
