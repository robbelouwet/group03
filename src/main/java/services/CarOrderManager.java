package services;


import domain.car.CarModel;
import domain.car.options.OptionSelector;
import domain.order.CarOrder;
import persistence.CarOrderRepository;
import persistence.CarCatalog;

import java.util.List;
import java.util.stream.Collectors;

public class CarOrderManager {
    private final CarOrderRepository carRepository;

    private final CarCatalog carCatalog = new CarCatalog();

    public CarOrderManager(CarOrderRepository repository) {
        this.carRepository = repository;
    }

    public List<CarOrder> getPendingOrders() {
        return carRepository.getOrders().stream()
                .filter(o -> !o.isFinished())
                .map(CarOrder::copy)
                .collect(Collectors.toList());
    }

    public List<CarOrder> getFinishedOrders() {
        return carRepository.getOrders().stream()
                .filter(CarOrder::isFinished)
                .map(CarOrder::copy)
                .collect(Collectors.toList());
    }

    public List<CarModel> getCarModels() {
        return carCatalog.getModels();
    }

    /**
     * Throws an exception if the optionSelector does not contain a valid selection for the carModel
     *
     * @return A copy of the CarOrder that is created
     */
    public CarOrder submitCarOrder(CarModel carModel, OptionSelector optionSelector) {
        CarOrder order = new CarOrder(carModel, optionSelector.getSelectedOptions());
        carRepository.addOrder(order);
        return order.copy();
    }
}
