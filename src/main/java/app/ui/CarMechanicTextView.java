package app.ui;

import app.controllers.MechanicController;
import app.ui.interfaces.ICarMechanicView;
import app.utils.ConsoleReader;

import java.util.List;
import java.util.Scanner;

public class CarMechanicTextView implements ICarMechanicView {
    private final MechanicController controller;
    private final Scanner scanner;

    public CarMechanicTextView() {
        this.controller = new MechanicController(this);
        this.scanner = new Scanner(System.in);
        initialize();
    }

    private void initialize() {
        System.out.println("Hi mechanic!");
        controller.showMainMenu();
    }

    @Override
    public void showWorkStations(List<String> availableWorkstations) {
        System.out.println("Available workstations:");
        for (String ws : availableWorkstations) {
            System.out.println("- Workstation [" + ws + "]");
        }

        String action = ConsoleReader.getInstance().ask("Select a workstation by typing its name: | Cancel [cancel]: ");
        while (!(isWorkStation(action, availableWorkstations) || action.equals("cancel"))) {
            System.out.println("This is not a valid option.");
            action = ConsoleReader.getInstance().ask("Select a workstation by typing its name: | Cancel [cancel]: ");
        }
        if (isWorkStation(action, availableWorkstations)) {
            String finalSelectedWorkstation = action;
            String selectedWorkStation = availableWorkstations.stream().filter(ws -> ws.equals(finalSelectedWorkstation)).findAny().orElseThrow();
            controller.selectWorkStation(selectedWorkStation);
        }
    }

    private boolean isWorkStation(String name, List<String> workStations) {
        return workStations.stream().anyMatch(ws -> ws.equals(name));
    }

    @Override
    public void showAvailableTasks(List<String> workStationTasks) {
        System.out.println("Available workstation tasks:");
        for (String at : workStationTasks) {
            System.out.println("-" + at);
        }

        String action = ConsoleReader.getInstance().ask("Select a task by typing its name: | Cancel [cancel]: ");
        while (!(isTaskName(action) || action.equals("cancel"))) {
            System.out.println("This is not a valid option.");
            action = ConsoleReader.getInstance().ask("Select a task by typing its name: | Cancel [cancel]: ");
        }

        if (isTaskName(action)) {
            controller.selectTask(action);
        } else {
            controller.showMainMenu();
        }
    }

    private boolean isTaskName(String name) {
        return controller.isTaskName(name);
    }

    @Override
    public void showTaskInfo(String info, List<String> actions) {
        System.out.println(info);
        System.out.println("Actions to complete this task:");
        for (String action: actions){
            System.out.println("-" + action);
        }

        String action = ConsoleReader.getInstance().ask("Finish task [finish] | Cancel [cancel]: ");
        while (!(action.equals("finish") || action.equals("cancel"))) {
            System.out.println("This is not a valid option.");
            action = ConsoleReader.getInstance().ask("Finish task [finish] | Cancel [cancel]: ");
        }
        if (action.equals("finish")) {
            controller.finishTask();
        } else {
            controller.showMainMenu();
        }
    }


}
