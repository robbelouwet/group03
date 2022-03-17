package services;

import domain.assembly.AssemblyLine;
import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;
import domain.scheduler.ProductionScheduler;
import lombok.Getter;
import persistence.DataSeeder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class AssemblyManager {
    @Getter
    private final AssemblyLine assemblyLine;

    public AssemblyManager(ProductionScheduler scheduler) {
        assemblyLine = new AssemblyLine(DataSeeder.defaultAssemblyLine(), scheduler);
    }



    public boolean advance(int timeSpent) {
        return assemblyLine.advance(timeSpent);
    }

    // TODO: Return type of Map<WorkStation, List<AssemblyTasks>>
    public List<List<AssemblyTask>> getPendingTasks() {
        return getFilteredTasks(t -> !t.isFinished());
    }

    public List<List<AssemblyTask>> getFinishedTasks() {
        return getFilteredTasks(AssemblyTask::isFinished);
    }

    private List<List<AssemblyTask>> getFilteredTasks(Predicate<AssemblyTask> predicate) {
        List<List<AssemblyTask>> filtered = new ArrayList<>();
        for (WorkStation ws : assemblyLine.getWorkStations()) {
            List<AssemblyTask> pTasks = new ArrayList<>(ws.getTasks().stream().filter(predicate).toList());
            filtered.add(pTasks);
        }
        return filtered;
    }
}
