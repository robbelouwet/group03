package app.presentation;

import app.ui.GarageHolderView;
import domain.car.CarModel;
import domain.car.CarOrder;
import services.CarOrderManager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AssemblyLineController {
    private final CarOrderManager carOrderManager = CarOrderManager.getInstance();
    private final GarageHolderView ui;

    private CarModel selectedModel;

    public AssemblyLineController(GarageHolderView ui) {
        this.ui = ui;
    }

    public void loginToSystem() {
        List<CarOrder> pendingOrders = carOrderManager.getPendingOrders();
        List<CarOrder> finishedOrders = carOrderManager.getFinishedOrders();
        ui.showOverview(pendingOrders.stream().map(CarOrder::toString).collect(Collectors.toList()), finishedOrders.stream().map(CarOrder::toString).collect(Collectors.toList()));
    }

    public void showModels() {
        ui.showCarModels(carOrderManager.getCarModels().stream().map(CarModel::getName).collect(Collectors.toList()));
    }

    public void selectModel(String model) {
        selectedModel = carOrderManager.getCarModels().stream().filter(m -> m.getName().equals(model)).findAny().orElseThrow();
        ui.showCarForm(selectedModel.getModelSpecification().getOptions());
    }

    public void submitCarOrder(Map<String, String> data) {
        if (selectedModel == null) {
            throw new IllegalStateException();
        }
        CarModel model = selectedModel;
        selectedModel = null;
        ui.showPredictedEndTime(carOrderManager.submitCarOrder(model, data).getEndTime());
    }
}
