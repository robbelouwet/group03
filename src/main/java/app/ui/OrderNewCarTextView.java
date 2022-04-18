package app.ui;

import app.controllers.OrderNewCarController;
import app.ui.interfaces.IOrderNewCarView;
import app.utils.ConsoleReader;
import domain.scheduler.DateTime;

import java.util.*;

public class OrderNewCarTextView implements IOrderNewCarView {
    private final OrderNewCarController controller;

    public OrderNewCarTextView(OrderNewCarController controller) {
        this.controller = controller;
        controller.setUi(this);
        initialize();
    }

    private void initialize() {
        ConsoleReader.getInstance().println("Hi garage holder!");
        controller.start();
    }

    @Override
    public boolean showOverview(List<String> pendingOrders, List<String> finishedOrders) {
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
        return action.equals("order");
    }

    @Override
    public String showCarModels(List<String> models) {
        ConsoleReader.getInstance().println("Carmodel options:");
        for (var model : models) {
            ConsoleReader.getInstance().println(model);
        }
        String model = ConsoleReader.getInstance().ask(("Type the name of a model to select it: "));
        while (!models.contains(model)) {
            model = ConsoleReader.getInstance().ask("Try again: ");
        }
        return model;
    }

    @Override
    public String showCarOption(String category, List<String> options) {
        ConsoleReader.getInstance().println("Select an option or cancel [cancel]");
        ConsoleReader.getInstance().println("Category: " + category);
        for (var value : options) {
            ConsoleReader.getInstance().println(value);
        }
        String value = ConsoleReader.getInstance().ask("Select a value: ");
        if (value.equals("cancel")) {
            return null;
        }
        while (!options.contains(value)) {
            value = ConsoleReader.getInstance().ask("Try again: ");
            if (value.equals("cancel")) {
                return null;
            }
        }
        return value;
    }

    @Override
    public boolean confirmOrder() {
        var val = ConsoleReader.getInstance().ask("Confirm order? [confirm] | [cancel]");
        while (!List.of("confirm", "cancel").contains(val)) {
            val = ConsoleReader.getInstance().ask("Try again: ");
        }
        return val.equals("confirm");
    }

    @Override
    public void showPredictedEndTime(DateTime endTime) {
        ConsoleReader.getInstance().println("Predicted end time: " + endTime);
    }
}
