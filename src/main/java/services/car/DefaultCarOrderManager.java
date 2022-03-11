package services.car;

import domain.OrderStatus;
import domain.scheduler.ProductionScheduler;
import domain.car.CarModel;
import domain.car.CarOrder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

// package-private!
class DefaultCarOrderManager extends CarOrderManager {

    @Getter // this needs to be a by-reference getter! Cloning every time defeats the singleton pattern
    private static final DefaultCarOrderManager instance = new DefaultCarOrderManager();

    private List<CarOrder> orders;
    private List<CarModel> carModels;
    private ProductionScheduler scheduler;


    @Override
    public List<CarOrder> getPendingOrders() {
        return orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.Pending)
                .map(CarOrder::clone)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarOrder> getFinishedOrders() {
        return orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.Finished)
                .map(CarOrder::clone)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarModel> getCarModels() {
        return carModels.stream()
                .map(CarModel::clone)
                .collect(Collectors.toList());
    }

    @Override
    public ProductionScheduler getScheduler() {
        return scheduler.clone();
    }
}
