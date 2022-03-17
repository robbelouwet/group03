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

/**
 * Class {@code AssemblyManager} is responsible for the {@code AssemblyLine}.
 * It can tell the assembly line to advance, and retrieve car orders & assembly tasks.
 */
public class AssemblyManager {
    private final AssemblyLine assemblyLine;

    public AssemblyManager(AssemblyLine aline) {
        assemblyLine = aline;
    }

    /**
     * This method will try to advance the assembly line forward with one step.
     *
     * @param timeSpent The time that was spent during the current phase in minutes (normally, a phase lasts 1 hour).
     */
    public void advance(int timeSpent) {
        assemblyLine.advance(timeSpent);
    }

    /**
     * This method retrieves the pending assembly tasks of the workstations on the assembly line.
     *
     * @return {@link Map} collection that contains {@code WorkStation} as a key and a {@code List&#60;AssemblyTask&#62;} as a value.
     * This means that the collection holds the pending assembly tasks per workstation.
     */
    // TODO: Map<WorkStation, List<AssemblyTask>>
    public Map<String, List<AssemblyTask>> getPendingTasks() {
        return getTasksbyFinished(false);
    }

    /**
     * This method retrieves the finished assembly tasks of the workstations on the assembly line.
     *
     * @return {@link Map} collection that contains {@code WorkStation} as a key and a {@code List&#60;AssemblyTask&#62;} as a value.
     * This means that the collection holds the finished assembly tasks per workstation.
     */
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

    /**
     * This method retrieves the new pending orders after the assembly line WOULD have moved.
     * This means that this is a best-case scenario and will retrieve all the pending orders but the {@code CarOrder} of the last workstation.
     *
     * @param pendingOrders All car orders that still need to be processed on the assembly line.
     * @return {@code List&#60;CarOrder&#62;} All car orders that still need to be processed on the
     *          assembly line without the car orders that would be finished after the assembly line moves one step forward.
     */
    public List<CarOrder> getSimulatedOrders(List<CarOrder> pendingOrders) {
        return pendingOrders.stream()
                .filter(o -> !assemblyLine.isPresentInLastWorkstation(o))
                .collect(Collectors.toList());
    }
}
