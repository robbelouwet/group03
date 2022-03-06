package services;

import domain.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CarOrderManager {

    @Getter // this needs to be a by-reference getter! Cloning every time defeats the singleton pattern
    private final CarOrderManager instance = new CarOrderManager();

    private List<CarOrder> orders;
    private List<CarModel> carModels;
    private ProductionScheduler scheduler;

    public List<CarOrder> getPendingOrders() {
        return orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.Pending)
                .map(CarOrder::clone)
                .collect(Collectors.toList());
    }

    public List<CarOrder> getFinishedOrders() {
        return orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.Finished)
                .map(CarOrder::clone)
                .collect(Collectors.toList());
    }

    public List<CarModel> getCarModels() {
        return carModels.stream()
                .map(CarModel::clone)
                .collect(Collectors.toList());
    }

    public ProductionScheduler getScheduler() {
        return scheduler.clone();
    }
}
