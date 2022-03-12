package services.car;


import domain.car.CarModel;
import domain.order.CarOrder;
import domain.time.TimeManager;
import lombok.Getter;
import persistence.CarOrderCatalog;
import persistence.CarRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CarOrderManager {
    @Getter
    private static final CarOrderManager instance = new CarOrderManager();

    private final CarRepository carRepository = new CarRepository();
    private final CarOrderCatalog carOrderCatalog;

    CarOrderManager() {
        carOrderCatalog = CarOrderCatalog.getInstance();
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
        CarOrder order = new CarOrder(TimeManager.getCurrentTime(), carModel, data);
        carOrderCatalog.addOrder(order);
        return order.clone();
    }
}
