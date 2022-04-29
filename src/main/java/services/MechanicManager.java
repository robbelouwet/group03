package services;

import domain.assembly.AssemblyLine;
import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A class that is responsible for keeping track of the current state of the mechanic using the system,
 * (i.e. the current work station and the selected task).
 */
public class MechanicManager {
    private WorkStation currentWorkStation;
    private AssemblyTask selectedTask;
    private final AssemblyLine assemblyLine;

    public MechanicManager(AssemblyLine assemblyLine) {
        this.assemblyLine = assemblyLine;
    }

    /**
     * Select a workstation with its name
     *
     * @param name the name of the workstation to select
     */
    public void selectWorkStation(String name) {
        this.currentWorkStation = assemblyLine.getBusyWorkstations().stream().filter(w -> w.getName().equals(name)).findAny().orElseThrow();
    }

    /**
     * Get a copy of the currently selected workstation
     */
    public WorkStation getCurrentWorkStation() {
        if (currentWorkStation != null) return currentWorkStation.copy();
        return null;
    }

    /**
     * Select a task based on its name within the select workstation
     * Throws an exception if there is no workstation selected or the name is not found within the tasks
     *
     * @param assemblyTaskName the name of the task
     * @return a copy of the selected task
     */
    public AssemblyTask selectTask(String assemblyTaskName) {
        if (currentWorkStation == null) throw new IllegalStateException("There is no current workstation selected.");
        selectedTask = currentWorkStation.getPendingTasks().stream().filter(at -> at.getName().equals(assemblyTaskName)).findAny().orElseThrow();
        return selectedTask.copy();
    }

    /**
     * Get a copy of the currently selected task
     */
    public AssemblyTask getSelectedTask() {
        return selectedTask == null ? null : selectedTask.copy();
    }

    /**
     * Finish the currently selected task
     *
     * @param timeSpent The time the mechanic spent on this single task
     */
    public void finishTask(int timeSpent) {
        if (selectedTask == null) throw new IllegalStateException("There is no selected task.");
        currentWorkStation.finishTask(selectedTask, timeSpent);
    }

    /**
     * Get all the tasks of the currently selected workstation
     */
    public List<String> getTaskNames() {
        if (currentWorkStation == null) throw new IllegalStateException("There is no current workstation selected.");
        return currentWorkStation.getTasksInformation();
    }

    /**
     * Check if the input string is the name of a task of the currently selected workstation
     *
     * @param name the name of the task
     */
    public boolean isValidTask(String name) {
        if (currentWorkStation == null) throw new IllegalStateException("There is no current workstation selected.");
        return currentWorkStation.getPendingTasks().stream().anyMatch(at -> at.getName().equals(name));
    }

    public List<WorkStation> getBusyWorkStations() {
        return assemblyLine.getBusyWorkstations().stream().map(WorkStation::copy).collect(Collectors.toList());
    }
}
