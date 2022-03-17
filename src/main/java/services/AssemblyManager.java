package services;

import domain.assembly.AssemblyLine;
import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;
import domain.order.CarOrder;
import domain.scheduler.ProductionScheduler;
import lombok.Getter;
import persistence.DataSeeder;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AssemblyManager {
    @Getter
    private final AssemblyLine assemblyLine;

    public AssemblyManager(ProductionScheduler scheduler) {
        assemblyLine = new AssemblyLine(DataSeeder.defaultAssemblyLine(), scheduler);
    }

    public AssemblyManager(AssemblyLine aline) {
        assemblyLine = aline;
    }

    public boolean advance(int timeSpent) {
        return assemblyLine.advance(timeSpent);
    }

    public Map<String, List<AssemblyTask>> getPendingTasks() {
        return getFilteredTasks(t -> !t.isFinished());
    }

    public Map<String, List<AssemblyTask>> getFinishedTasks() {
        return getFilteredTasks(AssemblyTask::isFinished);
    }

    private Map<String, List<AssemblyTask>> getFilteredTasks(Predicate<AssemblyTask> predicate) {
        Map<String, List<AssemblyTask>> pendingTasks = new HashMap<>();
        for (WorkStation ws : assemblyLine.getWorkStations()) {
            List<AssemblyTask> pTasks = new ArrayList<>(ws.getPendingTasks().stream().filter(predicate).toList());
            pendingTasks.put(ws.getName(), pTasks);
        }
        return pendingTasks;
    }

    private Map<String, List<AssemblyTask>> getTasksByFinished(boolean isFinished) {
        Map<String, List<AssemblyTask>> pendingTasks = new HashMap<>();
        for (WorkStation ws : assemblyLine.getWorkStations()) {
            List<AssemblyTask> pTasks = new ArrayList<>(ws.getPendingTasks().stream().filter(t -> isFinished).toList());
            pendingTasks.put(ws.getName(), pTasks);
        }
        return pendingTasks;
    }

    public List<CarOrder> getSimulatedOrders(List<CarOrder> pendingOrders) {
        return pendingOrders.stream()
                .filter(o -> !assemblyLine.orderMatchWithLastWorkStation(o))
                .collect(Collectors.toList());
    }

    public List<WorkStation> getBusyWorkStations() {
        return assemblyLine.getBusyWorkstations();
    }

}
