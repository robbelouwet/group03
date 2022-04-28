package services;

import domain.assembly.AssemblyLine;
import domain.scheduler.FIFOSchedulingAlgorithm;
import domain.scheduler.ProductionScheduler;
import domain.scheduler.TimeManager;
import lombok.Getter;
import persistence.CarOrderRepository;
import persistence.DataSeeder;

public class ManagerStore {
    @Getter
    private final AssemblyManager assemblyLineManager;
    @Getter
    private final CarOrderManager carOrderManager;
    @Getter
    private final MechanicManager mechanicManager;
    @Getter
    private final ProductionSchedulerManager productionSchedulerManager;

    public ManagerStore() {
        this(new CarOrderRepository());
    }

    public ManagerStore(CarOrderRepository repository) {
        CarOrderRepository carOrderRepository = repository.copy();
        var timemanager = new TimeManager();
        var scheduler = new ProductionScheduler(carOrderRepository, timemanager, new FIFOSchedulingAlgorithm());
        this.productionSchedulerManager = new ProductionSchedulerManager(scheduler, timemanager, carOrderRepository);
        var assemblyLine = new AssemblyLine(DataSeeder.defaultAssemblyLine(), scheduler, timemanager);
        assemblyLineManager = new AssemblyManager(assemblyLine);
        carOrderManager = new CarOrderManager(carOrderRepository);
        mechanicManager = new MechanicManager(assemblyLine);
    }
}
