package services.car;

import domain.car.CarModel;
import domain.car.CarOrder;
import lombok.Getter;
import persistence.CarOrderCatalog;
import persistence.CarRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// package-private!
class DefaultCarOrderManager extends CarOrderManager {

    @Getter // this needs to be a by-reference getter! Cloning every time defeats the singleton pattern
    private static final DefaultCarOrderManager instance = new DefaultCarOrderManager();
    private final CarRepository carRepository = new CarRepository();
    private final CarOrderCatalog carOrderCatalog;

    DefaultCarOrderManager() {
        carOrderCatalog = CarOrderCatalog.getInstance();
    }


    @Override
    public List<CarOrder> getPendingOrders() {
        return carOrderCatalog.getOrders().stream()
                .filter(o -> !o.isFinished())
                .map(CarOrder::clone)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarOrder> getFinishedOrders() {
        return carOrderCatalog.getOrders().stream()
                .filter(CarOrder::isFinished)
                .map(CarOrder::clone)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarModel> getCarModels() {
        return carRepository.getModels();
    }

    public CarOrder submitCarOrder(CarModel carModel, Map<String, String> data) {
        CarOrder order = new CarOrder(LocalDateTime.now(), carModel, data);
        carOrderCatalog.addOrder(order);
        return order.clone();
    }
}
