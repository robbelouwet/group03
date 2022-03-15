package app.ui;

import app.controllers.ManagerController;
import app.ui.interfaces.IManagerView;
import app.utils.ConsoleReader;

import java.util.List;
import java.util.Scanner;

public class ManagerTextView implements IManagerView {
    private final ManagerController managerController;
    private final Scanner scanner;

    public ManagerTextView() {
        managerController = new ManagerController(this);
        this.scanner = new Scanner(System.in);
        initialize();
    }

    private void initialize() {
        System.out.println("Hi manager!");
        managerController.showMainMenu();
    }

    @Override
    public void confirmMove(int timeSpent) {
        managerController.advanceAssemblyLine(timeSpent);
    }

    @Override
    public void showOverview(List<String> pendingOrders, List<String> simFinishedOrders, List<List<String>> pendingTasks, List<List<String>> finishedTasks) {
        System.out.println("Current Assembly Line Status:");
        for (var order : pendingOrders) {
            System.out.println(order);
        }

        System.out.println("Future Assembly Line Status:");
        for (var order : simFinishedOrders) {
            System.out.println(order);
        }

        System.out.println("Pending tasks of workstations:");
        printTasks(pendingTasks);

        System.out.println("Finished tasks of workstations:");
        printTasks(finishedTasks);

        String action = ConsoleReader.getInstance().ask("Advance Assembly Line? [yes] | [no]");
        while (!(action.equals("yes") || action.equals("no"))) {
            System.out.println("This is not a valid option.");
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

    private int askTimeSpent() {
        boolean correct = false;
        int result = 0;
        while (!correct) {
            String time = ConsoleReader.getInstance().ask("Time spent? [positive number]");
            try {
                result = Integer.parseInt(time);
                correct = result >= 0;
            } catch (Exception e) {
                System.out.println("That's not a valid number!");
            }
        }
        return result;
    }

    private void printTasks(List<List<String>> list) {
        for (int i = 0; i < list.size(); i++) {
            // TODO: Refactor to Map<String, List<String>> because workstation has a name not an index
            System.out.printf("Workstation %d:\n", i);
            for (int j = 0; j < list.get(i).size(); j++) {
                System.out.printf("\t%s\n", list.get(i).get(j));
            }
        }
    }
}
