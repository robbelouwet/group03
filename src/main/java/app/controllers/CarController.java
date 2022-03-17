package app.controllers;

import app.ui.interfaces.IGarageHolderView;
import domain.car.CarModel;
import domain.order.CarOrder;
import services.CarOrderManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CarController {
    private final CarOrderManager carOrderManager;
    private final IGarageHolderView ui;

    public CarController(IGarageHolderView ui) {
        this.ui = ui;
        carOrderManager = new CarOrderManager();
    }

    /**
     * Gets all pending and finished orders and notifies the ui to show some metadata about these orders
     */
    public void showMainMenu() {
        List<CarOrder> pendingOrders = carOrderManager.getPendingOrders();
        List<CarOrder> finishedOrders = carOrderManager.getFinishedOrders();
        ui.showOverview(pendingOrders.stream().map(CarOrder::toString).collect(Collectors.toList()), finishedOrders.stream().map(CarOrder::toString).collect(Collectors.toList()));
    }

    /**
     * Gets all available carmodels and notifies the ui to show the names of these models
     */
    public void showModels() {
        ui.showCarModels(carOrderManager.getCarModels().stream().map(CarModel::getName).collect(Collectors.toList()));
    }

    /**
     * Select a model for a carorder
     * Gets the specification for this model and notifies the ui it should show these specifications
     *
     * @param model the name of the selected model
     */
    public void selectModel(String model) {
        var selectedModel = carOrderManager.getCarModels().stream().filter(m -> m.getName().equals(model)).findAny().orElseThrow();

        carOrderManager.selectModel(selectedModel);
        ui.showCarForm(selectedModel.getModelSpecification().getOptions());
    }

    /**
     * Submit a car order
     * Throws an error when there is no model selected yet
     *
     * @param data a map which maps the option-key to the selected value
     */
    public void submitCarOrder(Map<String, String> data) {
        var carModel = carOrderManager.getSelectedModel();
        if (!carModel.isValidInputData(data)) {
            throw new IllegalArgumentException("The data object does not match the modelspecification!");
        }
        ui.showPredictedEndTime(carOrderManager.submitCarOrder(data).getEndTime());
    }
}
