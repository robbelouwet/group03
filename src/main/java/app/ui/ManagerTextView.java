package app.ui;

import app.controllers.ManagerController;
import app.ui.interfaces.IManagerView;
import app.utils.ConsoleReader;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ManagerTextView implements IManagerView {
    private final ManagerController managerController;

    public ManagerTextView(ManagerController controller) {
        managerController = controller;
        managerController.setUi(this);
        initialize();
    }

    private void initialize() {
        ConsoleReader.getInstance().println("Hi manager!");
        var askString = "Select menu option: \n\tAssembly Line Overview [assembly] \n\tScheduling Algorithm Overview [algorithm] \n\tCancel [cancel]:";
        String action = ConsoleReader.getInstance().ask(askString);

        while (!(action.equals("assembly") || action.equals("algorithm") || action.equals("cancel"))) {
            ConsoleReader.getInstance().println("This is not a valid option.");
            action = ConsoleReader.getInstance().ask(askString);
        }

        switch (action) {
            case "assembly" -> managerController.showAssemblyLineOverview();
            case "algorithm" -> managerController.showAlgorithmOverview();
            default -> {
            }
        }
    }

    @Override
    public void confirmMove(int timeSpent) {
        managerController.advanceAssemblyLine(timeSpent);
    }

    @Override
    public void showAdvanceOverview() {
        String action = ConsoleReader.getInstance().ask("Advance Assembly Line? [yes] | [no]");
        while (!(action.equals("yes") || action.equals("no"))) {
            ConsoleReader.getInstance().println("This is not a valid option.");
            action = ConsoleReader.getInstance().ask("Advance Assembly Line? [yes] | [no]");
        }
        if (action.equals("yes")) {
            int time = askTimeSpent();
            confirmMove(time);
        }
    }

    @Override
    public void showErrorMessage(String err) {
        System.err.println(err);
    }

    @Override
    public void showSchedulingAlgorithms(List<String> algorithms, String selectedAlgorithm) {
        ConsoleReader.getInstance().println("Possible Scheduling Algorithms:");
        for (String algorithm : algorithms) {
            StringBuilder string = new StringBuilder(String.format("%s", algorithm));
            if (algorithm.equals(selectedAlgorithm)) string.append(": (X)");
            ConsoleReader.getInstance().println(string.toString());
        }

        String askString = "Select an algorithm by typing its name: | Cancel [cancel]: ";
        String action = ConsoleReader.getInstance().ask(askString);

        while (!(algorithms.contains(action) || action.equals("cancel"))) {
            ConsoleReader.getInstance().println("This is not a valid option.");
            action = ConsoleReader.getInstance().ask(askString);
        }
        if (algorithms.contains(action)) {
            switch (action) {
                case "SB" -> {
                    managerController.showSpecificationBatchOrders(action);
                }
                default -> managerController.selectAlgorithm(action, Optional.empty());
            }
        }
    }

    @Override
    public void showPossibleOptionsForAlgorithm(List<Map<String, String>> options, String algorithm) {
        ConsoleReader.getInstance().println("Possible Car Options to Give Priority:");
        for (int i = 0; i < options.size(); i++) {
            var optionsSet = options.get(i);
            var optionsString = optionsSet.keySet().stream()
                    .map(key -> key + " = " + optionsSet.get(key))
                    .collect(Collectors.joining(", ", i + 1 + ". -> {", "}"));
            ConsoleReader.getInstance().println(optionsString);
        }
        int index = askCarOptionsIndex(options.size());
        Map<String, String> selectedOptions = options.get(index - 1);
        managerController.selectAlgorithm(algorithm, Optional.of(selectedOptions));
    }

    private int askCarOptionsIndex(int max) {
        String askString = "Select Car Options to Give Priority of Batch [number]: | Cancel [cancel]: ";
        boolean correct = false;
        int result = 0;
        while (!correct) {
            String number = ConsoleReader.getInstance().ask(askString);
            try {
                result = Integer.parseInt(number);
                correct = result >= 1 && result <= max;
                if (!correct) throw new Exception();
            } catch (Exception e) {
                ConsoleReader.getInstance().println("That's not a valid number!");
            }
        }
        return result;
    }

    private int askTimeSpent() {
        boolean correct = false;
        int result = 0;
        while (!correct) {
            String time = ConsoleReader.getInstance().ask("Time spent in minutes?");
            try {
                result = Integer.parseInt(time);
                correct = result >= 0;
            } catch (Exception e) {
                ConsoleReader.getInstance().println("That's not a valid number!");
            }
        }
        return result;
    }
}
