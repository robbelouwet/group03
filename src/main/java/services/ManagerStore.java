package services;

import domain.scheduler.ProductionScheduler;
import lombok.Getter;
import lombok.Setter;
import persistence.CarOrderRepository;

public class ManagerStore {
    @Getter @Setter
    private static ManagerStore instance = new ManagerStore();

    /**
     * TODO:
     * Setter can only be called from test package
     */
    @Getter
    private AssemblyManager assemblyLineManager;
    @Getter
    private CarOrderManager carOrderManager;
    @Getter
    private MechanicManager mechanicManager;

    public void init() {
        init(new CarOrderRepository());
    }

    public void init(CarOrderRepository repository) {
        CarOrderRepository carOrderRepository = repository.copy();

        var scheduler = new ProductionScheduler(carOrderRepository);

        assemblyLineManager = new AssemblyManager(scheduler);
        carOrderManager = new CarOrderManager(carOrderRepository);
        mechanicManager = new MechanicManager();
    }
}
