package services;

import domain.scheduler.ProductionScheduler;
import lombok.Getter;
import persistence.CarOrderRepository;

public class ManagerStore {
    @Getter
    private final AssemblyManager assemblyLineManager;
    @Getter
    private final CarOrderManager carOrderManager;
    @Getter
    private final MechanicManager mechanicManager;

    public ManagerStore() {
        this(new CarOrderRepository());
    }

    public ManagerStore(CarOrderRepository repository) {
        CarOrderRepository carOrderRepository = repository.copy();

        var scheduler = new ProductionScheduler(carOrderRepository);

        assemblyLineManager = new AssemblyManager(scheduler);
        carOrderManager = new CarOrderManager(carOrderRepository);
        mechanicManager = new MechanicManager();
    }
}
