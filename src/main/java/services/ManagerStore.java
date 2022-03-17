package services;

import domain.assembly.AssemblyLine;
import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;
import domain.scheduler.ProductionScheduler;
import lombok.Getter;
import persistence.CarOrderRepository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ManagerStore {
    /**
     * TODO:
     * Setter can only be called from test package
     */
    @Getter
    private static AssemblyManager assemblyLineManager;
    @Getter
    private static CarOrderManager carOrderManager;

    public static void init() {
        init(new CarOrderRepository());
    }

    public static void init(CarOrderRepository repository) {
        CarOrderRepository carOrderRepository = repository.copy();

        var scheduler = new ProductionScheduler(carOrderRepository);

        assemblyLineManager = new AssemblyManager(scheduler);
        carOrderManager = new CarOrderManager(carOrderRepository);
    }
}
