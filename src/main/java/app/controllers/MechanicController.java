package app.controllers;

import app.ui.interfaces.ICarMechanicView;
import domain.assembly.WorkStation;
import services.AssemblyManager;
import services.MechanicManager;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class {@code MechanicController} is responsible for the communication between the UI and the Domain.
 */
public class MechanicController extends AssemblyLineStatusController {
    private ICarMechanicView ui;
    private final MechanicManager mechanicManager;

    MechanicController(MechanicManager mechanicManager, AssemblyManager assemblyManager) {
        super(assemblyManager);
        this.mechanicManager = mechanicManager;
    }

    public void setUi(ICarMechanicView ui) {
        this.ui = ui;
    }

    /**
     * Provides the UI with the names of the workstations where work can be done, and triggers the UI to show them.
     */
    public void showMainMenu() {
        List<WorkStation> availableWorkstations = mechanicManager.getBusyWorkStations();
        ui.showWorkStations(availableWorkstations.stream().map(WorkStation::getName).collect(Collectors.toList()));
    }

    /**
     * Gets the workstation with given workStationName, and makes it the current workstation for the mechanic.
     * Throws a NoSuchElementException if no such workstation was found.
     * Provides the UI with the current workstation's pending tasks, and triggers the UI to show them.
     * @param workStationName The name of the workstation that was selected.
     */
    public void selectWorkStation(String workStationName){
        mechanicManager.selectWorkStation(workStationName);
        var ws = mechanicManager.getCurrentWorkStation();
        ui.showAvailableTasks(ws.getTasksInformation());
    }

    /**
     * Gets the assembly task with given assemblyTaskName, and makes it the selected assembly task for the mechanic.
     * Provides the UI with the assembly task's information and its action, and triggers the UI to show them.
     * @param assemblyTaskName The name of the workstation that was selected.
     */
    public void selectTask(String assemblyTaskName){
        var task = mechanicManager.selectTask(assemblyTaskName);
        var ws = mechanicManager.getCurrentWorkStation();
        ui.showTaskInfo(task.getInformation(ws.getCarOrder()), task.getActions());
    }

    /**
     * Finishes the mechanic's current task.
     * Provides the UI with the current workstation's pending tasks, and triggers the UI to show them.
     */
    public void finishTask(int timeSpent){
        mechanicManager.finishTask(timeSpent);
        ui.showAvailableTasks(mechanicManager.getTaskNames());
    }

    /**
     * Checks if given name is a valid task's name.
     * @param name The string that needs to be validated.
     * @return true if given name is a valid task's name.
     */
    public boolean isTaskName(String name) {
        return mechanicManager.isValidTask(name);
    }

    public void showAssemblyLineStatus() {
        super.showAssemblyLineStatus(ui);
    }
}
