package services;

import domain.OrderStatus;
import domain.ProductionScheduler;
import domain.car.Car;
import domain.car.CarModel;
import domain.car.CarOrder;
import lombok.Getter;
import persistence.CarRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CarOrderManager {
    private static CarOrderManager instance;

    private final CarRepository carRepository = new CarRepository();
    private final List<CarOrder> orders = new ArrayList<>();
    private final ProductionScheduler scheduler;

    private CarOrderManager() {
        scheduler = ProductionScheduler.getInstance();
    }

    public static CarOrderManager getInstance() {
        if (instance == null) {
            instance = new CarOrderManager();
        }
        return instance;
    }

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
        return carRepository.getModels();
    }

    public CarOrder submitCarOrder(CarModel carModel, Map<String, String> data) {
        CarOrder order = new CarOrder(LocalDateTime.now(), carModel, data);
        scheduler.addOrder(order);
        orders.add(order);
        return order.clone();
    }
}
