package app.ui;

import app.controllers.ManagerController;
import app.ui.interfaces.IManagerView;
import app.utils.ConsoleReader;

import java.util.List;
import java.util.Map;

public class ManagerTextView implements IManagerView {
    private final ManagerController managerController;

    public ManagerTextView(ManagerController controller) {
        managerController = controller;
        managerController.setUi(this);
        initialize();
    }

    private void initialize() {
        ConsoleReader.getInstance().println("Hi manager!");
        managerController.showMainMenu();
    }

    @Override
    public void confirmMove(int timeSpent) {
        managerController.advanceAssemblyLine(timeSpent);
    }

    @Override
    public void showOverview(List<String> pendingOrders, List<String> simFinishedOrders, Map<String, List<String>> pendingTasks, Map<String, List<String>> finishedTasks) {
        ConsoleReader.getInstance().println("Current Assembly Line Status:");
        for (var order : pendingOrders) {
            ConsoleReader.getInstance().printf("\t%s\n", order);
        }
        ConsoleReader.getInstance().println("Future Assembly Line Status:");
        for (var order : simFinishedOrders) {
            ConsoleReader.getInstance().printf("\t%s\n", order);
        }

        ConsoleReader.getInstance().println("Pending tasks of workstations:");
        printTasks(pendingTasks);

        ConsoleReader.getInstance().println("Finished tasks of workstations:");
        printTasks(finishedTasks);

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
    public void showAssemblyLineStatusAfterMove(List<String> pendingOrders) {
        ConsoleReader.getInstance().println("Assembly Line Status after the Move:");
        for (var order : pendingOrders) {
            ConsoleReader.getInstance().println(order);
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

    private void printTasks(Map<String, List<String>> list) {
        list.forEach((k, v) -> {
            ConsoleReader.getInstance().printf("\tWorkstation %s:\n", k);
            for (int i = 0; i < v.size(); i++) {
                ConsoleReader.getInstance().printf("\t\t%s\n", list.get(k).get(i));
            }
        });
    }
}
