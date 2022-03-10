package app.ui;

import app.presentation.GarageHolderUIController;
import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.car.CarOrder;

import java.util.*;

public class GarageHolderUI {
    // TODO obviously this class needs some work, I just made this for testing
    private final GarageHolderUIController controller;
    private final Scanner scanner;

    public static void main(String[] args) {
        try {
            new GarageHolderUI(new Scanner(System.in));
        } catch (IllegalStateException | NoSuchElementException e) {
            System.out.println("Exiting application");
        }
    }

    public GarageHolderUI(Scanner scanner) {
        this.scanner = scanner;
        controller = new GarageHolderUIController(this);
        controller.loginToSystem();
    }

    public void showOverview(List<CarOrder> pendingOrders, List<CarOrder> finishedOrders) {
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
        } else {
            return;
        }
    }

    private boolean isModelName(String name, List<CarModel> models) {
        return models.stream().anyMatch(m -> m.getName().equals(name));
    }

    public void showCarModels(List<CarModel> models) {
        System.out.println("Carmodel options:");
        for (var model : models) {
            System.out.println(model.getName());
        }
        System.out.print("Type the name of a model to select it: ");
        String model = scanner.nextLine();
        while (!isModelName(model, models)) {
            System.out.print("Try again: ");
            model = scanner.nextLine();
        }
        String finalModel = model;
        CarModel carModel = models.stream().filter(m -> m.getName().equals(finalModel)).findAny().orElseThrow();
        controller.selectModel(carModel);
    }

    private boolean isValue(String value, List<String> values) {
        return values.stream().anyMatch(v -> v.equals(value));
    }

    public void showCarForm(CarModelSpecification specification) {
        Map<String, List<String>> options = specification.getOptions();
        System.out.println("Make a selection for each option, or type [cancel]");
        Map<String, String> selection = new HashMap<>();
        for (var key : options.keySet()) {
            System.out.println(key);
            for (var value : options.get(key)) {
                System.out.println(value);
            }
            System.out.print("Select a value: ");
            String value = scanner.nextLine();
            if (value.equals("cancel")) {
                controller.loginToSystem();
                return;
            }
            while (!isValue(value, options.get(key))) {
                System.out.print("Try again: ");
                value = scanner.nextLine();
                if (value.equals("cancel")) {
                    controller.loginToSystem();
                    return;
                }
            }
            selection.put(key, value);
        }
        controller.submitCarOrder(selection);
    }

    public void showPredictedEndTime(CarOrder order) {
        System.out.println("Predicted end time: " + order.getEndTime());
        controller.loginToSystem();
    }
}
