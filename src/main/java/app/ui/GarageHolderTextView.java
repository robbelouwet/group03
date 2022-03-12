package app.ui;

import app.controllers.CarController;
import app.ui.interfaces.IGarageHolderView;
import domain.time.DateTime;

import java.time.LocalDateTime;
import java.util.*;

public class GarageHolderTextView implements IGarageHolderView {
    // TODO obviously this class needs some work, I just made this for testing
    private final CarController controller;
    private final Scanner scanner;


    public GarageHolderTextView() {
        this.scanner = new Scanner(System.in);
        controller = new CarController(this);
        initialize();
    }

    private void initialize(){
        System.out.println("Hi garage holder!");
    }

    @Override
    public void showOverview(List<String> pendingOrders, List<String> finishedOrders) {
        System.out.println("Pending orders:");
        for (var order : pendingOrders) {
            System.out.println(order);
        }
        System.out.println("Finished orders:");
        for (var order : finishedOrders) {
            System.out.println(order);
        }
        System.out.print("Make an order [order] | Cancel [cancel]: ");
        String action = scanner.nextLine();
        while (!(action.equals("order") || action.equals("cancel"))) {
            System.out.println("This is not a valid option.");
            System.out.print("Make an order [order] | Cancel [cancel]: ");
            action = scanner.nextLine();
        }
        if (action.equals("order")) {
            controller.showModels();
        }
    }

    @Override
    public boolean isModelName(String name, List<String> models) {
        return models.stream().anyMatch(m -> m.equals(name));
    }

    @Override
    public void showCarModels(List<String> models) {
        System.out.println("Carmodel options:");
        for (var model : models) {
            System.out.println(model);
        }
        System.out.print("Type the name of a model to select it: ");
        String model = scanner.nextLine();
        while (!isModelName(model, models)) {
            System.out.print("Try again: ");
            model = scanner.nextLine();
        }
        String finalModel = model;
        String carModel = models.stream().filter(m -> m.equals(finalModel)).findAny().orElseThrow();
        controller.selectModel(carModel);
    }

    @Override
    public boolean isValue(String value, List<String> values) {
        return values.stream().anyMatch(v -> v.equals(value));
    }

    @Override
    public void showCarForm(Map<String, List<String>> options) {
        System.out.println("Make a selection for each option, or type [cancel]");
        Map<String, String> selection = new HashMap<>();
        for (var key : options.keySet()) {
            System.out.println("Option: " + key);
            for (var value : options.get(key)) {
                System.out.println(value);
            }
            System.out.print("Select a value: ");
            String value = scanner.nextLine();
            if (value.equals("cancel")) {
                controller.showMainMenu();
                return;
            }
            while (!isValue(value, options.get(key))) {
                System.out.print("Try again: ");
                value = scanner.nextLine();
                if (value.equals("cancel")) {
                    controller.showMainMenu();
                    return;
                }
            }
            selection.put(key, value);
        }
        controller.submitCarOrder(selection);
    }

    @Override
    public void showPredictedEndTime(DateTime endTime) {
        System.out.println("Predicted end time: " + endTime);
        controller.showMainMenu();
    }
}
