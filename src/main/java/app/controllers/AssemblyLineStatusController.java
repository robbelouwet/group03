package app.controllers;

import app.ui.interfaces.IAssemblyLineStatusView;
import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;
import domain.order.CarOrder;
import services.AssemblyManager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class {@code AssemblyLineStatusController} is responsible for the overlapping use case of showing the
 * status of the assembly line. Since the roles Manager and Car Mechanic need to be able to see the status.
 *
 * GRASP: The controller pattern assigns the responsibility of dealing with system events to a non-UI class
 * that represents the overall system or a use case scenario. A controller object is a non-user interface
 * object responsible for receiving or handling a system event.
 */
public abstract class AssemblyLineStatusController {
    private final AssemblyManager assemblyManager;

    protected AssemblyLineStatusController(AssemblyManager assemblyManager) {
        this.assemblyManager = assemblyManager;
    }

    public void showAssemblyLineStatus(IAssemblyLineStatusView ui){
        Map<WorkStation, CarOrder> currentOrdersOnAssemblyLine = assemblyManager.getOrdersOnAssemblyLine();
        Map<WorkStation, List<AssemblyTask>> pendingTasks = assemblyManager.getPendingTasks();
        Map<WorkStation, List<AssemblyTask>> finishedTasks = assemblyManager.getFinishedTasks();
        ui.showAssemblyLineStatus(
                convertToStringMap(currentOrdersOnAssemblyLine),
                convertToStringList(pendingTasks),
                convertToStringList(finishedTasks));
    }

    private Map<String, String> convertToStringMap(Map<WorkStation, CarOrder> orders) {
        Map<String, String> convertedOrders = new LinkedHashMap<>();
        orders.forEach((k, v) -> convertedOrders.put(k.getName(), v.toString()));
        return convertedOrders;
    }

    /**
     * This method converts the Map<WorkStation, List<AssemblyTask>> to Map<String, List<String>>
     * This is because the view can not know of domain elements
     */
    private Map<String, List<String>> convertToStringList(Map<WorkStation, List<AssemblyTask>> pendingTasks) {
        Map<String, List<String>> tasks = new LinkedHashMap<>();
        pendingTasks.forEach((k, v) -> tasks.put(k.getName(), k.getTasksInformation(v) ));
        return tasks;
    }
}
