package app.controllers;

import app.ui.interfaces.ICarMechanicView;
import services.CarOrderManager;
import services.ManagerFactory;

public class MechanicController {
    private final CarOrderManager carOrderManager = ManagerFactory.getInstance().getCarOrderManager();
    private final ICarMechanicView view;

    public MechanicController(ICarMechanicView view) {
        this.view = view;
    }
}
