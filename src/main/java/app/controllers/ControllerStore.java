package app.controllers;

import lombok.Getter;
import services.ManagerStore;

public class ControllerStore {
    @Getter
    private final ManagerController managerController;
    @Getter
    private final OrderNewCarController carController;
    @Getter
    private final MechanicController mechanicController;

    public ControllerStore() {
        this(new ManagerStore());
    }

    public ControllerStore(ManagerStore managerStore) {
        managerController = new ManagerController(managerStore.getAssemblyLineManager(), managerStore.getProductionSchedulerManager());
        carController = new OrderNewCarController(managerStore.getCarOrderManager());
        mechanicController = new MechanicController(managerStore.getMechanicManager(), managerStore.getAssemblyLineManager());
    }
}
