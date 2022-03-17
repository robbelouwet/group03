package services;

import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

public class MechanicManager {
    @Setter @Getter
    private WorkStation currentWorkStation;
    @Getter @Setter
    private AssemblyTask selectedTask;

    public AssemblyTask selectTask(String assemblyTaskName) {
        selectedTask = currentWorkStation.getPendingTasks().stream().filter(at -> at.getName().equals(assemblyTaskName)).findAny().orElseThrow();
        return selectedTask;
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
