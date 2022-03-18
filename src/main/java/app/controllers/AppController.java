package app.controllers;

import app.ui.CarMechanicTextView;
import app.ui.GarageHolderTextView;
import app.ui.ManagerTextView;
import app.ui.interfaces.IAppView;
import app.utils.ConsoleReader;

public class AppController {
    private final IAppView appView;

    public AppController(IAppView view) {
        appView = view;
    }

    public void initialize() {
        loop();
    }

    private void loop() {
        String role = appView.showMenu();

        boolean quit = false;
        switch (role) {
            case "manager" -> appView.showManager();
            case "garage holder" -> appView.showGarageHolder();
            case "mechanic" -> appView.showMechanic();
            case "quit" -> quit = true;
            default -> ConsoleReader.getInstance().println("Sorry, don't know that role");
        }
        if (quit) return;
        loop();
    }
}
