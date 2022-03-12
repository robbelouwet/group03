package app.controllers;

import app.ui.interfaces.ICarMechanicView;
import services.car.CarOrderManager;

public class MechanicController {
    private final CarOrderManager carOrderManager = CarOrderManager.getInstance();
    private final ICarMechanicView view;

    public MechanicController(ICarMechanicView view) {
        this.view = view;
    }
}
