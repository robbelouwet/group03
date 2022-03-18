package app.controllers;

import app.ui.interfaces.ICarMechanicView;
import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;
import services.AssemblyManager;
import services.ManagerStore;
import services.MechanicManager;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class {@code MechanicController} is responsible for the communication between the UI and the Domain.
 */
public class MechanicController {
    private final ICarMechanicView view;
    private final MechanicManager mechanicManager;
    private final AssemblyManager assemblyManager;

    public MechanicController(ICarMechanicView view) {
        this.view = view;
        mechanicManager = ManagerStore.getInstance().getMechanicManager();
        assemblyManager = ManagerStore.getInstance().getAssemblyLineManager();
    }

    /**
     * Provides the UI with the names of the workstations where work can be done, and triggers the UI to show them.
     */
    public void showMainMenu() {
        List<WorkStation> availableWorkstations = assemblyManager.getBusyWorkStations();
        view.showWorkStations(availableWorkstations.stream().map(WorkStation::getName).collect(Collectors.toList()));
    }

    /**
     * Gets the workstation with given workStationName, and makes it the current workstation for the mechanic.
     * Throws a NoSuchElementException if no such workstation was found.
     * Provides the UI with the current workstation's pending tasks, and triggers the UI to show them.
     * @param workStationName The name of the workstation that was selected.
     */
    public void selectWorkStation(String workStationName){
        var ws = assemblyManager.getBusyWorkStations().stream()
                .filter(w -> w.getName().equals(workStationName))
                .findAny().orElseThrow();
        mechanicManager.setCurrentWorkStation(ws);
        view.showAvailableTasks(mechanicManager.getCurrentWorkStation().getPendingTasks().stream().map(AssemblyTask::toString).collect(Collectors.toList()));
    }

    /**
     * Gets the assembly task with given assemblyTaskName, and makes it the selected assembly task for the mechanic.
     * Provides the UI with the assembly task's information and its action, and triggers the UI to show them.
     * @param assemblyTaskName The name of the workstation that was selected.
     */
    public void selectTask(String assemblyTaskName){
        var task = mechanicManager.selectTask(assemblyTaskName);
        view.showTaskInfo(task.getTaskInformation(), task.getActions());
    }

    /**
     * Finishes the mechanic's current task.
     * Provides the UI with the current workstation's pending tasks, and triggers the UI to show them.
     */
    public void finishTask(){
        mechanicManager.finishTask();
        view.showAvailableTasks(mechanicManager.getTaskNames());
    }


    /**
     * Checks if given name is a valid task's name.
     * @param name The string that needs to be validated.
     * @return true if given name is a valid task's name.
     */
    public boolean isTaskName(String name) {
        return mechanicManager.isValidTask(name);
    }
}
