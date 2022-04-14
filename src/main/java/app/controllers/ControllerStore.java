package app.controllers;

import lombok.Getter;
import services.ManagerStore;

public class ControllerStore {
    @Getter
    private final ManagerController managerController;
    @Getter
    private final CarController carController;
    @Getter
    private final MechanicController mechanicController;
    //@Getter
    //private final AssemblyLineStatusController assemblyLineStatusController;

    public ControllerStore() {
        this(new ManagerStore());
    }

    public ControllerStore(ManagerStore managerStore) {
        //assemblyLineStatusController = new AssemblyLineStatusController(managerStore.getAssemblyLineManager());
        managerController = new ManagerController(managerStore.getAssemblyLineManager());
        carController = new CarController(managerStore.getCarOrderManager());
        mechanicController = new MechanicController(managerStore.getMechanicManager(), managerStore.getAssemblyLineManager());
    }
}
