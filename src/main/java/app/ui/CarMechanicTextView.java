package app.ui;

import app.controllers.MechanicController;
import app.ui.interfaces.ICarMechanicView;
import app.utils.ConsoleReader;
import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;

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
    public void showWorkStations(List<WorkStation> availableWorkstations) {
        System.out.println("Available workstations:");
        for (WorkStation ws : availableWorkstations) {
            System.out.println("-" + ws);
        }

        String action = ConsoleReader.getInstance().ask("Select a workstation by typing its name: | Cancel [cancel]: ");
        while (!(WorkStation.isWorkstationName(action, availableWorkstations) || action.equals("cancel"))) {
            System.out.println("This is not a valid option.");
            action = ConsoleReader.getInstance().ask("Select a workstation by typing its name: | Cancel [cancel]: ");
        }
        if (!action.equals("cancel")) {
            WorkStation selectedWorkStation = WorkStation.getWorkStationByName(action, availableWorkstations);
            controller.selectWorkStation(selectedWorkStation);
        }
    }

    @Override
    public void showAvailableTasks(List<AssemblyTask> workStationTasks) {
        System.out.println("Available workstation tasks:");
        for (AssemblyTask at : workStationTasks) {
            System.out.println("-" + at);
        }

        String action = ConsoleReader.getInstance().ask("Select a task by typing its name: | Cancel [cancel]: ");
        while (!(AssemblyTask.isTaskName(action, workStationTasks) || action.equals("cancel"))) {
            System.out.println("This is not a valid option.");
            action = ConsoleReader.getInstance().ask("Select a task by typing its name: | Cancel [cancel]: ");
        }

        if (!action.equals("cancel")) {
            AssemblyTask selectedTask = AssemblyTask.getAssemblyTaskByName(action, workStationTasks);
            controller.selectTask(selectedTask);
        } else {
            controller.showMainMenu();
        }
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
