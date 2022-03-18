package services;

import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A class that is responsible for keeping track of the current state of the mechanic using the system,
 * (i.e. the current work station and the selected task).
 */
public class MechanicManager {
    @Setter @Getter
    private WorkStation currentWorkStation;
    @Getter @Setter
    private AssemblyTask selectedTask;

    public AssemblyTask selectTask(String assemblyTaskName) {
        if (currentWorkStation == null) throw new IllegalStateException("There is no current workstation selected.");
        selectedTask = currentWorkStation.getPendingTasks().stream().filter(at -> at.getName().equals(assemblyTaskName)).findAny().orElseThrow();
        return selectedTask;
    }

    public void finishTask() {
        if (selectedTask == null) throw new IllegalStateException("There is no selected task.");
        selectedTask.finishTask();
    }

    public List<String> getTaskNames() {
        if (currentWorkStation == null) throw new IllegalStateException("There is no current workstation selected.");
        return currentWorkStation.getPendingTasks().stream().map(AssemblyTask::toString).collect(Collectors.toList());
    }

    public boolean isValidTask(String name) {
        if (currentWorkStation == null) throw new IllegalStateException("There is no current workstation selected.");
        return currentWorkStation.getPendingTasks().stream().anyMatch(at -> at.getName().equals(name));
    }
}
