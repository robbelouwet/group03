package app.ui;

import app.controllers.MechanicController;
import app.ui.interfaces.ICarMechanicView;

public class CarMechanicTextView implements ICarMechanicView {
    private final MechanicController mechanicController;

    public CarMechanicTextView() {
        this.mechanicController = new MechanicController(this);
        initialize();
    }

    private void initialize(){
        System.out.println("Hi mechanic!");
    }
}
