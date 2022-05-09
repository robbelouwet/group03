package app.ui;

import app.controllers.ManagerController;
import app.ui.interfaces.IManagerView;
import app.utils.ConsoleReader;

import java.util.List;
import java.util.Map;
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
        var askString = """
                Select menu option:
                \tScheduling Algorithm Overview [algorithm]
                \tShow statistics [statistics]
                \tCancel [cancel]:""";
        String action = ConsoleReader.getInstance().ask(askString);

        while (!(action.equals("algorithm") || action.equals("cancel") || action.equals("statistics"))) {
            ConsoleReader.getInstance().println("This is not a valid option.");
            action = ConsoleReader.getInstance().ask(askString);
        }

        switch (action) {
            case "algorithm" -> managerController.showAlgorithmOverview();
            case "statistics" -> managerController.getStatistics();
            default -> {
            }
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
            if ("SB".equals(action)) {
                managerController.showSpecificationBatchOrders(action);
            } else {
                managerController.selectAlgorithm(action, new AlgorithmOptionsWrapper(Map.of()));
            }
        } else if (!action.equals("cancel")) showErrorMessage("This algorithm doesn't exist!");
    }

    @Override
    public void showPossibleOptionsForAlgorithm(List<Map<String, String>> options, String algorithm) {
        if (options.isEmpty()) {
            ConsoleReader.getInstance().println("No selectedOptions possible... Choose other algorithm");
            return;
        }
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
        boolean success = managerController.selectAlgorithm(algorithm, new AlgorithmOptionsWrapper(selectedOptions));
        if (!success)
            showErrorMessage("Something went wrong with selecting the algorithm!");
    }

    @Override
    public void showStatistics(String statistics) {
        ConsoleReader.getInstance().println(statistics);
    }

    /**
     * For every unique key-value pairs of selectedOptions we want to choose 1 and validate
     * if the input number is correct.
     */
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
}
