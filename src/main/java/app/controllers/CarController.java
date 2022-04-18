package app.controllers;

import app.ui.interfaces.IGarageHolderView;
import domain.car.CarModel;
import domain.car.options.Option;
import domain.car.options.OptionCategory;
import domain.order.CarOrder;
import services.CarOrderManager;

import java.util.List;
import java.util.stream.Collectors;

public class CarController {
    private final CarOrderManager carOrderManager;
    private IGarageHolderView ui;

    CarController(CarOrderManager carOrderManager) {
        this.carOrderManager = carOrderManager;
    }

    public void setUi(IGarageHolderView ui) {
        this.ui = ui;
    }

    /**
     * Start the car controller
     */
    public void start() {
        showMainMenu();
    }

    /**
     * Gets all pending and finished orders and notifies the ui to show some metadata about these orders
     */
    private void showMainMenu() {
        List<CarOrder> pendingOrders = carOrderManager.getPendingOrders();
        List<CarOrder> finishedOrders = carOrderManager.getFinishedOrders();
        while (ui.showOverview(pendingOrders.stream().map(CarOrder::toString).collect(Collectors.toList()), finishedOrders.stream().map(CarOrder::toString).collect(Collectors.toList()))) {
            showModels();
            pendingOrders = carOrderManager.getPendingOrders();
            finishedOrders = carOrderManager.getFinishedOrders();
        }
    }

    /**
     * Gets all available carmodels and notifies the ui to show the names of these models
     */
    private void showModels() {
        var model = ui.showCarModels(carOrderManager.getCarModels().stream().map(CarModel::getName).collect(Collectors.toList()));
        selectModel(model);
    }

    /**
     * Select a model for a carorder
     * Gets the specification for this model and notifies the ui it should show these specifications
     *
     * @param model the name of the selected model
     */
    private void selectModel(String model) {
        var selectedModel = carOrderManager.getCarModels().stream().filter(m -> m.getName().equals(model)).findAny().orElseThrow();

        carOrderManager.selectModel(selectedModel);
        askNextOption();
    }

    private OptionCategory getNextCategory() {
        for (var cat : carOrderManager.getOptionSelector().getNotSelectedCategories().keySet()) return cat;
        return null;
    }

    private void askNextOption() {
        var cat = getNextCategory();
        if (cat != null) {
            var selector = carOrderManager.getOptionSelector();
            var options = selector.getNotSelectedCategories().get(cat);
            var option = ui.showCarOption(cat.getName(), options.stream().map(Option::name).collect(Collectors.toList()));
            if (option == null) {
                carOrderManager.cancelCarOrder();
                return;
            }
            selector.selectOption(options.stream().filter(option1 -> option1.name().equals(option)).findAny().orElseThrow());
            askNextOption();
        }
        else {
            if (ui.confirmOrder()) {
                carOrderManager.submitCarOrder();
            } else {
                carOrderManager.cancelCarOrder();
            }
        }
    }
}
