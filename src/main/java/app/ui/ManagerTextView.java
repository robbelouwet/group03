package app.ui;

import app.controllers.ManagerController;
import app.ui.interfaces.IManagerView;
import app.utils.ConsoleReader;

import java.util.List;

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
        if (algorithms.contains(action)){
            managerController.selectAlgorithm(action);
        }
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
