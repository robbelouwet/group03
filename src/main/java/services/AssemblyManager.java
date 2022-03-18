package services;

import domain.assembly.AssemblyLine;
import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;
import domain.order.CarOrder;
import domain.scheduler.ProductionScheduler;
import lombok.Getter;
import persistence.DataSeeder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Class {@code AssemblyManager} is responsible for the {@code AssemblyLine}.
 * It can tell the assembly line to advance, and retrieve car orders & assembly tasks.
 */
public class AssemblyManager {
    @Getter
    private final AssemblyLine assemblyLine;

    public AssemblyManager(ProductionScheduler scheduler) {
        assemblyLine = new AssemblyLine(DataSeeder.defaultAssemblyLine(), scheduler);
    }

    public AssemblyManager(AssemblyLine aline) {
        assemblyLine = aline;
    }

    /**
     * This method will try to advance the assembly line forward with one step.
     *
     * @param timeSpent The time that was spent during the current phase in minutes (normally, a phase lasts 1 hour).
     */
    public boolean advance(int timeSpent) {
        return assemblyLine.advance(timeSpent, false);
    }

    /**
     * This method retrieves the pending assembly tasks of the workstations on the assembly line.
     *
     * @return {@link Map} collection that contains {@code WorkStation} as a key and a {@code List&#60;AssemblyTask&#62;} as a value.
     * This means that the collection holds the pending assembly tasks per workstation.
     */
    public Map<WorkStation, List<AssemblyTask>> getPendingTasks() {
        return getFilteredTasks(t -> !t.isFinished());
    }

    /**
     * This method retrieves the finished assembly tasks of the workstations on the assembly line.
     *
     * @return {@link Map} collection that contains {@code WorkStation} as a key and a {@code List&#60;AssemblyTask&#62;} as a value.
     * This means that the collection holds the finished assembly tasks per workstation.
     */
    public Map<WorkStation, List<AssemblyTask>> getFinishedTasks() {
        return getFilteredTasks(AssemblyTask::isFinished);
    }

    private Map<WorkStation, List<AssemblyTask>> getFilteredTasks(Predicate<AssemblyTask> predicate) {
        Map<WorkStation, List<AssemblyTask>> pendingTasks = new LinkedHashMap<>();
        for (WorkStation ws : assemblyLine.getWorkStations()) {
            if (ws.getCarOrder() != null) {
                List<AssemblyTask> pTasks = new ArrayList<>(ws.getAllTasks().stream().filter(predicate).toList());
                if (pTasks.size() > 0) {
                    pendingTasks.put(ws, pTasks);
                }
            }
        }
        return pendingTasks;
    }

    /**
     * This method retrieves the new pending orders after the assembly line WOULD have moved.
     * This means that this is a best-case scenario and will retrieve all the pending orders but the {@code CarOrder} of the last workstation.
     *
     * @return {@code List&#60;CarOrder&#62;} All car orders that still need to be processed on the
     * assembly line without the car orders that would be finished after the assembly line moves one step forward.
     */
    public Map<WorkStation, CarOrder> getSimulatedOrders() {
        AssemblyLine copy = assemblyLine.copy();
        copy.advance(0, true);
        return getOrdersOnAssemblyLine(copy);
    }

    private Map<WorkStation, CarOrder> mapWorkstationsToTheNext(Map<WorkStation, CarOrder> orders){
        Map<WorkStation, CarOrder> mappedOrders = new LinkedHashMap<>();
        orders.forEach((k, v) -> {
            var wss = assemblyLine.getWorkStations();
            for (int i = 0; i < wss.size(); i++) {
                if (wss.get(i).equals(k) && i + 1 < wss.size()){
                    mappedOrders.put(wss.get(i+1), v);
                }
            }
        });
        return mappedOrders;
    }

    public Map<WorkStation, CarOrder> getOrdersOnAssemblyLine() {
        return getOrdersOnAssemblyLine(assemblyLine);
    }

    private Map<WorkStation, CarOrder> getOrdersOnAssemblyLine(AssemblyLine assemblyLine) {
        Map<WorkStation, CarOrder> ordersOnAssembly = new LinkedHashMap<>();
        for (WorkStation ws : assemblyLine.getWorkStations()) {
            if (ws.getCarOrder() != null) {
                ordersOnAssembly.put(ws, ws.getCarOrder());
            }
        }
        return ordersOnAssembly;
    }

    public List<WorkStation> getBusyWorkStations() {
        return assemblyLine.getBusyWorkstations();
    }

}
