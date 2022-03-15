package services.assembly;

import domain.assembly.AssemblyLine;
import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;
import domain.order.CarOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AssemblyManager {
    private final AssemblyLine assemblyLine;

    public AssemblyManager(AssemblyLine aline) {
        assemblyLine = aline;
    }

    public void advance(int timeSpent) {
        assemblyLine.advance(timeSpent);
    }

    public Map<String, List<AssemblyTask>> getPendingTasks() {
        return getTasksbyFinished(false);
    }

    public Map<String, List<AssemblyTask>> getFinishedTasks() {
        return getTasksbyFinished(true);
    }

    private Map<String, List<AssemblyTask>> getTasksbyFinished(boolean isFinished) {
        Map<String, List<AssemblyTask>> pendingTasks = new HashMap<>();
        for (WorkStation ws : assemblyLine.getWorkStations()) {
            List<AssemblyTask> pTasks = new ArrayList<>(ws.getTasks().stream().filter(t -> isFinished).toList());
            pendingTasks.put(ws.getName(), pTasks);
        }
        return pendingTasks;
    }

    public List<CarOrder> getSimulatedOrders(List<CarOrder> pendingOrders) {
        return pendingOrders.stream()
                .filter(o -> !assemblyLine.orderMatchWithLastWorkStation(o))
                .collect(Collectors.toList());
    }
}
