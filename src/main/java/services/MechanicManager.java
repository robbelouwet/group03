package services;

import domain.assembly.AssemblyLine;
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
    private WorkStation currentWorkStation;
    private AssemblyTask selectedTask;
    private final  AssemblyLine assemblyLine;

    public MechanicManager(AssemblyLine assemblyLine) {
        this.assemblyLine = assemblyLine;
    }

    public void selectWorkStation(String name) {
        this.currentWorkStation = assemblyLine.getWorkStations().stream().filter(w -> w.getName().equals(name)).findAny().orElseThrow();
    }

    public AssemblyTask selectTask(String assemblyTaskName) {
        selectedTask = currentWorkStation.getPendingTasks().stream().filter(at -> at.getName().equals(assemblyTaskName)).findAny().orElseThrow();
        return selectedTask.copy();
    }

    public void finishTask() {
        selectedTask.finishTask();
    }

    public List<String> getTaskNames() {
        return currentWorkStation.getPendingTasks().stream().map(AssemblyTask::toString).collect(Collectors.toList());
    }

    public boolean isValidTask(String name) {
        return currentWorkStation.getPendingTasks().stream().anyMatch(at -> at.getName().equals(name));
    }
}
