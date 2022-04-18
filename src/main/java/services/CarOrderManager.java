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

    private CarModel selectedModel;
    private OptionSelector optionSelector;
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
     * Throws an exception if getSelectedModel() == null
     * After this call getSelectedModel() and getOptionSelector() will be reset to null
     *
     * @return A copy of the CarOrder that is created
     */
    public CarOrder submitCarOrder() {
        if (selectedModel == null || getOptionSelector() == null) {
            throw new IllegalStateException("There was no model selected!");
        }

        CarOrder order = new CarOrder(selectedModel, optionSelector.getSelectedOptions());
        carRepository.addOrder(order);
        selectedModel = null;
        optionSelector = null;
        return order.copy();
    }

    /**
     * After this call getSelectedModel() and getOptionSelector() will be reset to null
     */
    public void cancelCarOrder() {
        selectedModel = null;
        optionSelector = null;
    }

    /**
     * After this call getSelectedModel() == model and getOptionSelector() is set to the correct optionSelector
     *
     * @param model
     */
    public void selectModel(CarModel model) {
        selectedModel = model;
        optionSelector = model.getModelSpecification().getOptionSelector();
    }

    public CarModel getSelectedModel() {
        return selectedModel;
    }

    public OptionSelector getOptionSelector() {
        return optionSelector;
    }
}
