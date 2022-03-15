package services.assembly;

import domain.assembly.AssemblyLine;
import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;

import java.util.ArrayList;
import java.util.List;

public class AssemblyManager {
    private final AssemblyLine assemblyLine;

    public AssemblyManager(AssemblyLine aline) {
        assemblyLine = aline;
    }

    public boolean advance(int timeSpent) {
        return assemblyLine.advance(timeSpent);
    }

    // TODO: anyone tips for cleaner and less re-use of methods below?
    // TODO: Return type of Map<WorkStation, List<AssemblyTasks>>
    public List<List<AssemblyTask>> getPendingTasks() {
        List<List<AssemblyTask>> pendingTasks = new ArrayList<>();
        for (WorkStation ws : assemblyLine.getWorkStations()) {
            List<AssemblyTask> pTasks = new ArrayList<>(ws.getTasks().stream().filter(t -> !t.isFinished()).toList());
            pendingTasks.add(pTasks);
        }
        return pendingTasks;
    }

    public List<List<AssemblyTask>> getFinishedTasks() {
        List<List<AssemblyTask>> finishedTasks = new ArrayList<>();
        for (WorkStation ws : assemblyLine.getWorkStations()) {
            List<AssemblyTask> pTasks = new ArrayList<>(ws.getTasks().stream().filter(AssemblyTask::isFinished).toList());
            finishedTasks.add(pTasks);
        }
        return finishedTasks;
    }
}
