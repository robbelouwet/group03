package app.ui;

import app.controllers.MechanicController;
import app.ui.interfaces.ICarMechanicView;
import app.utils.ConsoleReader;

import java.util.List;

public class CarMechanicTextView implements ICarMechanicView {
    private final MechanicController controller;

    public CarMechanicTextView(MechanicController controller) {
        this.controller = controller;
        controller.setUi(this);
        initialize();
    }

    private void initialize() {
        ConsoleReader.getInstance().println("Hi mechanic!");
        var askString = "Select menu option: \n\tAssembly Line Status [status] | Perform Tasks [tasks] | Cancel [cancel]:";
        String action = ConsoleReader.getInstance().ask(askString);

        while (!(action.equals("status") || action.equals("tasks") || action.equals("cancel"))) {
            ConsoleReader.getInstance().println("This is not a valid option.");
            action = ConsoleReader.getInstance().ask(askString);
        }

        switch (action) {
            case "status" -> controller.showAssemblyLineStatus();
            case "tasks" -> controller.showMainMenu();
            default -> {
            }
        }
    }

    @Override
    public void showWorkStations(List<String> availableWorkstations) {
        ConsoleReader.getInstance().println("Available workstations:");
        for (String ws : availableWorkstations) {
            ConsoleReader.getInstance().println("- Workstation [" + ws + "]");
        }

        String action = ConsoleReader.getInstance().ask("Select a workstation by typing its name: | Cancel [cancel]: ");
        while (!(isWorkStation(action, availableWorkstations) || action.equals("cancel"))) {
            ConsoleReader.getInstance().println("This is not a valid option.");
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
        ConsoleReader.getInstance().println("Available workstation tasks:");
        for (String at : workStationTasks) {
            ConsoleReader.getInstance().println("-" + at);
        }

        String action = ConsoleReader.getInstance().ask("Select a task by typing its name: | Cancel [cancel]: ");
        while (!(isTaskName(action) || action.equals("cancel"))) {
            ConsoleReader.getInstance().println("This is not a valid option.");
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
        ConsoleReader.getInstance().println(info);
        ConsoleReader.getInstance().println("Actions to complete this task:");
        for (String action : actions) {
            ConsoleReader.getInstance().println("-" + action);
        }

        String action = ConsoleReader.getInstance().ask("Finish task [finish] | Cancel [cancel]: ");
        while (!(action.equals("finish") || action.equals("cancel"))) {
            ConsoleReader.getInstance().println("This is not a valid option.");
            action = ConsoleReader.getInstance().ask("Finish task [finish] | Cancel [cancel]: ");
        }
        if (action.equals("finish")) {
            int timeSpent = askTimeSpent();
            controller.finishTask(timeSpent);
        } else {
            controller.showMainMenu();
        }
    }

    // TODO == gekopieerde code van ManagerTextView
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
