package app.controllers;

import app.ui.interfaces.IOrderNewCarView;
import domain.car.CarModel;
import domain.car.options.Option;
import domain.car.options.OptionCategory;
import domain.car.options.OptionSelector;
import domain.order.CarOrder;
import services.CarOrderManager;

import java.util.List;
import java.util.stream.Collectors;

public class OrderNewCarController {
    private final CarOrderManager carOrderManager;
    private IOrderNewCarView ui;

    OrderNewCarController(CarOrderManager carOrderManager) {
        this.carOrderManager = carOrderManager;
    }

    public void setUi(IOrderNewCarView ui) {
        this.ui = ui;
    }

    /**
     * Start the controller
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
        var selectedModel = carOrderManager.getCarModels().stream().filter(m -> m.getName().equals(model)).findAny().orElseThrow();
        OptionSelector selector = selectedModel.getOptionSelector();
        askNextOption(selectedModel, selector);
    }

    /**
     * Gets the first category for which a selection should be made
     *
     * @param optionSelector The optionSelector from which to find the next category
     * @return The next category
     */
    private OptionCategory getNextCategory(OptionSelector optionSelector) {
        for (var cat : optionSelector.getNotSelectedCategories().keySet()) return cat;
        return null;
    }

    private void askNextOption(CarModel carModel, OptionSelector optionSelector) {
        var cat = getNextCategory(optionSelector);
        if (cat != null) {  // If we are not finished selecting selectedOptions
            var options = optionSelector.getNotSelectedCategories().get(cat);  // Get the available selectedOptions in this category
            var option = ui.showCarOption(cat.getName(), options.stream().map(Option::name).collect(Collectors.toList()));
            if (option == null) {  // If we decided to cancel, return
                return;
            }
            optionSelector.selectOption(options.stream().filter(option1 -> option1.name().equals(option)).findAny().orElseThrow());
            askNextOption(carModel, optionSelector);
        } else {  // We are finished selecting selectedOptions
            if (ui.confirmOrder()) {  // Ask the user to confirm
                var order = carOrderManager.submitCarOrder(carModel, optionSelector);
                ui.showPredictedEndTime(order.getEndTime());
            }
        }
        // Return to the beginning of this use case
    }
}
