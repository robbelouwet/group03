package domain.scheduler;

import domain.car.CarOrder;
import lombok.Getter;

public abstract class ProductionScheduler {
    @Getter
    // inject strategy instance
    private static final ProductionScheduler instance = new FCFSProductionScheduler();

    public abstract void addOrder(CarOrder order);

    public abstract CarOrder getNextOrder();

    public abstract void updateSchedule(CarOrder process);

    @Override
    public ProductionScheduler clone(){
        return instance.clone();
    }
}
