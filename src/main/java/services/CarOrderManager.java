package services;

import domain.OrderStatus;
import domain.ProductionScheduler;
import domain.car.Car;
import domain.car.CarModel;
import domain.car.CarOrder;
import lombok.Getter;
import persistence.CarOrderCatalog;
import persistence.CarOrderCatalogObserver;
import persistence.CarRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CarOrderManager {
    private static CarOrderManager instance;

    private final CarRepository carRepository = new CarRepository();
    private final CarOrderCatalog carOrderCatalog;

    private CarOrderManager() {
        carOrderCatalog = CarOrderCatalog.getInstance();
    }

    public static CarOrderManager getInstance() {
        if (instance == null) {
            instance = new CarOrderManager();
        }
        return instance;
    }

    public List<CarOrder> getPendingOrders() {
        return carOrderCatalog.getOrders().stream()
                .filter(o -> !o.isFinished())
                .map(CarOrder::clone)
                .collect(Collectors.toList());
    }

    public List<CarOrder> getFinishedOrders() {
        return carOrderCatalog.getOrders().stream()
                .filter(CarOrder::isFinished)
                .map(CarOrder::clone)
                .collect(Collectors.toList());
    }

    public List<CarModel> getCarModels() {
        return carRepository.getModels();
    }

    public CarOrder submitCarOrder(CarModel carModel, Map<String, String> data) {
        CarOrder order = new CarOrder(LocalDateTime.now(), carModel, data);
        carOrderCatalog.addOrder(order);
        return order.clone();
    }
}
