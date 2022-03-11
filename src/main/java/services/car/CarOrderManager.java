package services.car;

import domain.scheduler.ProductionScheduler;
import domain.car.CarModel;
import domain.car.CarOrder;
import lombok.Getter;

import java.util.List;

public abstract class CarOrderManager {

    @Getter
    // inject concrete instance
    private static final CarOrderManager instance = new DefaultCarOrderManager();

    public abstract List<CarOrder> getPendingOrders();

    public abstract List<CarOrder> getFinishedOrders();

    public abstract List<CarModel> getCarModels();

    public abstract ProductionScheduler getScheduler();
}
