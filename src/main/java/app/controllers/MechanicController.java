package app.controllers;

import app.ui.GarageHolderTextView;
import domain.car.CarModel;
import domain.order.CarOrder;
import services.car.CarOrderManager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MechanicController {
    private final CarOrderManager carOrderManager = CarOrderManager.getInstance();
    private final GarageHolderTextView view;

    private CarModel selectedModel;

    public MechanicController(GarageHolderTextView view) {
        this.view = view;
    }

    public void loginToSystem() {
        List<CarOrder> pendingOrders = carOrderManager.getPendingOrders();
        List<CarOrder> finishedOrders = carOrderManager.getFinishedOrders();
        view.showOverview(pendingOrders.stream().map(CarOrder::toString).collect(Collectors.toList()), finishedOrders.stream().map(CarOrder::toString).collect(Collectors.toList()));
    }

    public void showModels() {
        view.showCarModels(carOrderManager.getCarModels().stream().map(CarModel::getName).collect(Collectors.toList()));
    }

    public void selectModel(String model) {
        selectedModel = carOrderManager.getCarModels().stream().filter(m -> m.getName().equals(model)).findAny().orElseThrow();
        view.showCarForm(selectedModel.getModelSpecification().getOptions());
    }

    public void submitCarOrder(Map<String, String> data) {
        if (selectedModel == null) {
            throw new IllegalStateException();
        }
        CarModel model = selectedModel;
        selectedModel = null;
        view.showPredictedEndTime(carOrderManager.submitCarOrder(model, data).getEndTime());
    }
}
