package services;

import domain.assembly.AssemblyLine;
import domain.scheduler.FIFOSchedulingAlgorithm;
import domain.scheduler.ProductionScheduler;
import domain.scheduler.TimeManager;
import lombok.Getter;
import persistence.CarOrderRepository;
import persistence.DataSeeder;

import java.util.LinkedList;

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
        var scheduler = new ProductionScheduler(carOrderRepository, timemanager, new LinkedList<>(), new FIFOSchedulingAlgorithm());
        var assemblyLine = new AssemblyLine(DataSeeder.defaultAssemblyLine(), scheduler);
        this.productionSchedulerManager = new ProductionSchedulerManager(scheduler, timemanager, carOrderRepository);
        assemblyLineManager = new AssemblyManager(assemblyLine);
        carOrderManager = new CarOrderManager(carOrderRepository);
        mechanicManager = new MechanicManager(assemblyLine);
    }
}
