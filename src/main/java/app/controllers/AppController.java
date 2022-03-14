package app.controllers;

import app.ui.CarMechanicTextView;
import app.ui.GarageHolderTextView;
import app.ui.ManagerTextView;
import app.ui.interfaces.IAppView;

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
            case "manager" -> new ManagerTextView();
            case "garage holder" -> new GarageHolderTextView();
            case "mechanic" -> new CarMechanicTextView();
            case "quit" -> quit = true;
            default -> System.out.println("Sorry, don't know that role");
        }
        if (quit) return;
        loop();
    }
}
