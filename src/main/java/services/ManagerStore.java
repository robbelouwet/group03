package services;

import domain.assembly.AssemblyLine;
import domain.scheduler.FIFOScheduler;
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

    public ManagerStore() {
        this(new CarOrderRepository());
    }

    public ManagerStore(CarOrderRepository repository) {
        CarOrderRepository carOrderRepository = repository.copy();
        var scheduler = ProductionScheduler.of(carOrderRepository, "FIFO");
        var assemblyLine = new AssemblyLine(DataSeeder.defaultAssemblyLine(), scheduler);
        assemblyLineManager = new AssemblyManager(assemblyLine);
        carOrderManager = new CarOrderManager(carOrderRepository);
        mechanicManager = new MechanicManager(assemblyLine);
    }
}
