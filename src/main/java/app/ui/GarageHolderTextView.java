package app.ui;

import app.controllers.CarController;
import app.ui.interfaces.IGarageHolderView;
import app.utils.ConsoleReader;
import domain.scheduler.DateTime;

import java.util.*;

public class GarageHolderTextView implements IGarageHolderView {
    private final CarController controller;

    public GarageHolderTextView(CarController controller) {
        this.controller = controller;
        controller.setUi(this);
        initialize();
    }

    private void initialize() {
        ConsoleReader.getInstance().println("Hi garage holder!");
        controller.showMainMenu();
    }

    @Override
    public void showOverview(List<String> pendingOrders, List<String> finishedOrders) {
        ConsoleReader.getInstance().println("Pending orders:");
        for (var order : pendingOrders) {
            ConsoleReader.getInstance().println(order);
        }
        ConsoleReader.getInstance().println("Finished orders:");
        for (var order : finishedOrders) {
            ConsoleReader.getInstance().println(order);
        }
        String action = ConsoleReader.getInstance().ask("Make an order [order] | Cancel [cancel]: ");
        while (!(action.equals("order") || action.equals("cancel"))) {
            ConsoleReader.getInstance().println("This is not a valid option.");
            action = ConsoleReader.getInstance().ask("Make an order [order] | Cancel [cancel]: ");
        }
        if (action.equals("order")) {
            controller.showModels();
        }
    }

    private boolean isModelName(String name, List<String> models) {
        return models.stream().anyMatch(m -> m.equals(name));
    }

    @Override
    public void showCarModels(List<String> models) {
        ConsoleReader.getInstance().println("Carmodel options:");
        for (var model : models) {
            ConsoleReader.getInstance().println(model);
        }
        String model = ConsoleReader.getInstance().ask(("Type the name of a model to select it: "));
        while (!isModelName(model, models)) {
            model = ConsoleReader.getInstance().ask("Try again: ");
        }
        String finalModel = model;
        String carModel = models.stream().filter(m -> m.equals(finalModel)).findAny().orElseThrow();
        controller.selectModel(carModel);
    }


    private boolean isValue(String value, List<String> values) {
        return values.stream().anyMatch(v -> v.equals(value));
    }

    @Override
    public void showCarForm(Map<String, List<String>> options) {
        ConsoleReader.getInstance().println("Make a selection for each option, or type [cancel]");
        Map<String, String> selection = new HashMap<>();
        for (var key : options.keySet()) {
            ConsoleReader.getInstance().println("Option: " + key);
            for (var value : options.get(key)) {
                ConsoleReader.getInstance().println(value);
            }
            String value = ConsoleReader.getInstance().ask("Select a value: ");
            if (value.equals("cancel")) {
                controller.showMainMenu();
                return;
            }
            while (!isValue(value, options.get(key))) {
                ConsoleReader.getInstance().print("Try again: ");
                value = ConsoleReader.getInstance().ask("Try again: ");
                if (value.equals("cancel")) {
                    controller.showMainMenu();
                    return;
                }
            }
            selection.put(key, value);
        }
        var confirm = ConsoleReader.getInstance().ask("Confirm order? [confirm] | [cancel]: ");
        while (!(confirm.equals("confirm") || confirm.equals("cancel"))) {
            ConsoleReader.getInstance().print("Try again: ");
        }
        if (confirm.equals("confirm")) {
            controller.submitCarOrder(selection);
        } else {
            controller.showMainMenu();
        }
    }

    @Override
    public void showPredictedEndTime(DateTime endTime) {
        ConsoleReader.getInstance().println("Predicted end time: " + endTime);
        controller.showMainMenu();
    }
}
