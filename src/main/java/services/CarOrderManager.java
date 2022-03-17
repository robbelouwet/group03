package services;


import domain.car.CarModel;
import domain.order.CarOrder;
import domain.time.TimeManager;
import persistence.CarOrderRepository;
import persistence.CarCatalog;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CarOrderManager {
    private final CarCatalog carCatalog;
    private final CarOrderRepository carRepository;

    private CarModel selectedModel;

    public CarOrderManager() {
        carCatalog = CarCatalog.getInstance();
        carRepository = new CarOrderRepository();
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

    public List<CarModel> getCarModels()  {
        return carCatalog.getModels();
    }

    /**
     * Throws an exception if getSelectedModel() == null
     * After this call getSelectedModel() will be reset to null
     *
     * @param data a map that maps the option-name to the selected value
     * @return A copy of the CarOrder that is created
     */
    public CarOrder submitCarOrder(Map<String, String> data) {
        if (selectedModel == null) {
            throw new IllegalStateException("There was no model selected!");
        }

        CarOrder order = new CarOrder(TimeManager.getCurrentTime(), selectedModel, data);
        carRepository.addOrder(order);
        selectedModel = null;
        return order.copy();
    }

    /**
     * After this call getSelectedModel() == model
     *
     * @param model
     */
    public void selectModel(CarModel model) {
        selectedModel = model;
    }

    public CarModel getSelectedModel() {
        return selectedModel;
    }
}
