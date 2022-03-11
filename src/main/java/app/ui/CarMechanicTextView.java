package app.ui;

import app.ui.interfaces.ICarMechanicView;
import services.assembly.AssemblyManager;

public class CarMechanicTextView implements ICarMechanicView {
    private final AssemblyManager assemblyManager = AssemblyManager.getInstance();

    public CarMechanicTextView() {
        // TODO: create controller and/or view?
    }
}
