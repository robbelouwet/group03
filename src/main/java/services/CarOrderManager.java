package services;


import domain.car.CarModel;
import domain.car.options.OptionSelector;
import domain.order.CarOrder;
import persistence.CarCatalog;
import persistence.CarOrderRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CarOrderManager {
    private final CarOrderRepository carRepository;

    private final CarCatalog carCatalog = new CarCatalog();

    private final Comparator<CarOrder> sorter = (o1, o2) -> -o1.getOrderTime().compareTo(o2.getOrderTime());  // This is defined here, because the same sorting is used for different functions

    public CarOrderManager(CarOrderRepository repository) {
        this.carRepository = repository;
    }

    /**
     * @return A list of all pending orders sorted by most recent first
     */
    public List<CarOrder> getPendingOrders() {
        return carRepository.getOrders().stream()
                .filter(o -> !o.isFinished())
                .map(CarOrder::copy)
                .sorted(sorter)
                .collect(Collectors.toList());
    }

    /**
     * @return A list of all finished orders sorted by most recent first
     */
    public List<CarOrder> getFinishedOrders() {
        return carRepository.getOrders().stream()
                .filter(CarOrder::isFinished)
                .map(CarOrder::copy)
                .sorted(sorter)
                .collect(Collectors.toList());
    }

    /**
     * @return A list of all car models in the application
     */
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
