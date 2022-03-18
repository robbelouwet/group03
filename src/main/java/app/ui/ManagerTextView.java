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
    public void showOverview(Map<String, String> pendingOrders, Map<String, String> simFinishedOrders, Map<String, List<String>> pendingTasks, Map<String, List<String>> finishedTasks) {
        ConsoleReader.getInstance().printf("Current Assembly Line Status: %s\n", pendingOrders.size() > 0 ? "" : "Empty");
        printOrders(pendingOrders);

        ConsoleReader.getInstance().printf("Future Assembly Line Status: %s\n", simFinishedOrders.size() > 0 ? "" : "Empty");
        printOrders(simFinishedOrders);

        ConsoleReader.getInstance().printf("Pending tasks of workstations: %s\n", pendingTasks.size() > 0 ? "" : "Nothing");
        printTasks(pendingTasks);

        ConsoleReader.getInstance().printf("Finished tasks of workstations: %s\n", finishedTasks.size() > 0 ? "" : "Nothing");
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
    public void showAssemblyLineStatusAfterMove(Map<String, String> pendingOrders) {
        ConsoleReader.getInstance().printf("Assembly Line Status after the Move: %s\n", pendingOrders.size() > 0 ? "" : "Empty");
        printOrders(pendingOrders);
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

    private void printOrders(Map<String, String> list) {
        list.forEach((k, v) -> {
            ConsoleReader.getInstance().printf("\tWorkstation %s: ", k);
            ConsoleReader.getInstance().printf("%s\n", list.get(k));
        });
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
