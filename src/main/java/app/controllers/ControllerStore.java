package app.controllers;

import lombok.Getter;
import services.ManagerStore;

public class ControllerStore {
    @Getter
    private final ManagerController managerController;
    @Getter
    private final OrderNewCarController orderNewCarController;
    @Getter
    private final MechanicController mechanicController;
    @Getter
    private final CheckOrderDetailsController checkOrderDetailsController;

    public ControllerStore() {
        this(new ManagerStore());
    }

    public ControllerStore(ManagerStore managerStore) {
        managerController = new ManagerController(managerStore.getAssemblyLineManager());
        orderNewCarController = new OrderNewCarController(managerStore.getCarOrderManager());
        mechanicController = new MechanicController(managerStore.getMechanicManager(), managerStore.getAssemblyLineManager());
        checkOrderDetailsController = new CheckOrderDetailsController(managerStore.getCarOrderManager());
    }
}
