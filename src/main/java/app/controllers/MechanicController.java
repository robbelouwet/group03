package app.controllers;

import app.ui.interfaces.ICarMechanicView;
import services.CarOrderManager;

public class MechanicController {
    private final CarOrderManager carOrderManager;
    private final ICarMechanicView view;

    public MechanicController(ICarMechanicView view) {
        this.view = view;
        carOrderManager = new CarOrderManager();
    }
}
