package services;

import domain.assembly.AssemblyLine;
import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;
import domain.order.CarOrder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class {@code AssemblyManager} is responsible for the {@code AssemblyLine}.
 * It can tell the assembly line to advance, and retrieve car orders & assembly tasks.
 */
public class AssemblyManager {
    private final AssemblyLine assemblyLine;

    public AssemblyManager(AssemblyLine aline) {
        assemblyLine = aline;
    }

    private interface WorkStationTaskMapper {
        List<AssemblyTask> wsToTasks(WorkStation ws);
    }

    /**
     * This method retrieves the pending assembly tasks of the workstations on the assembly line.
     *
     * @return {@link Map} collection that contains {@code WorkStation} as a key and a {@code List&#60;AssemblyTask&#62;} as a value.
     * This means that the collection holds the pending assembly tasks per workstation.
     */
    public Map<WorkStation, List<AssemblyTask>> getPendingTasks() {
        return getFilteredTasks(WorkStation::getPendingTasks);
    }

    /**
     * This method retrieves the finished assembly tasks of the workstations on the assembly line.
     *
     * @return {@link Map} collection that contains {@code WorkStation} as a key and a {@code List&#60;AssemblyTask&#62;} as a value.
     * This means that the collection holds the finished assembly tasks per workstation.
     */
    public Map<WorkStation, List<AssemblyTask>> getFinishedTasks() {
        return getFilteredTasks(WorkStation::getFinishedTasks);
    }

    private Map<WorkStation, List<AssemblyTask>> getFilteredTasks(WorkStationTaskMapper workStationTaskMapper) {
        Map<WorkStation, List<AssemblyTask>> pendingTasks = new LinkedHashMap<>();
        for (WorkStation ws : assemblyLine.getWorkStations()) {
            if (ws.getCarOrder() != null) {
                List<AssemblyTask> pTasks = workStationTaskMapper.wsToTasks(ws).stream().map(AssemblyTask::copy).toList();
                if (pTasks.size() > 0) {
                    pendingTasks.put(ws.copy(), pTasks);
                }
            }
        }
        return pendingTasks;
    }

    public Map<WorkStation, CarOrder> getOrdersOnAssemblyLine() {
        return getOrdersOnAssemblyLine(assemblyLine);
    }

    private Map<WorkStation, CarOrder> getOrdersOnAssemblyLine(AssemblyLine assemblyLine) {
        Map<WorkStation, CarOrder> ordersOnAssembly = new LinkedHashMap<>();
        for (WorkStation ws : assemblyLine.getWorkStations()) {
            if (ws.getCarOrder() != null) {
                ordersOnAssembly.put(ws.copy(), ws.getCarOrder().copy());
            }
        }
        return ordersOnAssembly;
    }

    public List<WorkStation> getBusyWorkStations() {
        return assemblyLine.getBusyWorkstations().stream().map(WorkStation::copy).collect(Collectors.toList());
    }
}
