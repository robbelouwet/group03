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
        String role = appView.showMenu();

        switch (role) {
            case "manager" -> new ManagerTextView();
            case "garage holder" -> new GarageHolderTextView();
            case "mechanic" -> new CarMechanicTextView();
            default -> System.out.println("Sorry, don't know that role");
        }
    }
}
