package app.presentation;

import app.ui.GarageHolderUI;
import domain.car.CarModel;
import domain.car.CarOrder;
import services.CarOrderManager;

import java.util.List;
import java.util.Map;

public class GarageHolderUIController {
    private final CarOrderManager carOrderManager = CarOrderManager.getInstance();
    private final GarageHolderUI ui;

    private CarModel selectedModel;

    public GarageHolderUIController(GarageHolderUI ui) {
        this.ui = ui;
    }

    public void loginToSystem() {
        List<CarOrder> pendingOrders = carOrderManager.getPendingOrders();
        List<CarOrder> finishedOrders = carOrderManager.getFinishedOrders();
        ui.showOverview(pendingOrders, finishedOrders);
    }

    public void showModels() {
        ui.showCarModels(carOrderManager.getCarModels());
    }

    public void selectModel(CarModel model) {
        selectedModel = model;
        ui.showCarForm(model.getModelSpecification());
    }

    public void submitCarOrder(Map<String, String> data) {
        if (selectedModel == null) {
            throw new IllegalStateException();
        }
        CarModel model = selectedModel;
        selectedModel = null;
        ui.showPredictedEndTime(carOrderManager.submitCarOrder(model, data));
    }
}
