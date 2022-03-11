package domain.scheduler;

import domain.assembly.AssemblyProcess;
import domain.car.CarOrder;
import lombok.Getter;

public abstract class ProductionScheduler {
    @Getter
    // inject strategy instance
    private static final ProductionScheduler instance = new FCFSProductionScheduler();

    public abstract void addOrder(CarOrder order);

    public abstract AssemblyProcess getNextProcess();

    public abstract void updateSchedule(AssemblyProcess process);

    @Override
    public ProductionScheduler clone(){
        return instance.clone();
    }
}
