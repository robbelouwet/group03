package app.ui;

import app.controllers.ManagerController;
import app.ui.interfaces.IManagerView;

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

        System.out.println("Advance Assembly Line? [yes] | [no]");
        String action = scanner.nextLine();
        while (!(action.equals("yes") || action.equals("no"))) {
            System.out.println("This is not a valid option.");
            System.out.println("Advance Assembly Line? [yes] | [no]");
            action = scanner.nextLine();
        }
        if (action.equals("yes")) {
            int time = askTimeSpent();
            confirmMove(time);
        }
    }

    private int askTimeSpent() {
        boolean correct = false;
        int result = 0;
        while (!correct) {
            System.out.println("Time spent? [positive number]");
            try {
                String time = scanner.nextLine();
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
