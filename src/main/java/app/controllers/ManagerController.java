package app.controllers;

import app.ui.interfaces.IManagerView;
import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;
import domain.order.CarOrder;
import services.AssemblyManager;
import services.ManagerStore;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class {@code ManagerController} is responsible for the communication between the UI and the Domain.
 */
public class ManagerController {
    private final AssemblyManager assemblyManager;
    private final IManagerView ui;

    public ManagerController(IManagerView ui) {
        this.ui = ui;
        this.assemblyManager = ManagerStore.getInstance().getAssemblyLineManager();
    }

    /**
     * Provides the UI with the pending & finished tasks and orders of the assembly line.
     */
    public void showMainMenu() {
        var ordersOnAssembly = assemblyManager.getOrdersOnAssemblyLine();
        Map<String, String> currentOrdersOnAssemblyLine = convertToStringMap(ordersOnAssembly);
        Map<String, String> simFinishedOrders = convertToStringMap(assemblyManager.getSimulatedOrders());
        Map<String, List<String>> pendingTasks = convertToStringList(assemblyManager.getPendingTasks());
        Map<String, List<String>> finishedTasks = convertToStringList(assemblyManager.getFinishedTasks());
        ui.showOverview(
                currentOrdersOnAssemblyLine,
                simFinishedOrders,
                pendingTasks,
                finishedTasks
        );
    }

    private Map<String, String> convertToStringMap(Map<WorkStation, CarOrder> orders){
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
        pendingTasks.forEach((k, v) -> tasks.put(k.getName(), v.stream().map(AssemblyTask::toString).collect(Collectors.toList())));
        return tasks;
    }

    /**
     * This method will try to advance the assembly line forward with one step.
     * If the assembly line has been successfully moved forward, this method will show an overview of the new assembly line status.
     *
     * @param timeSpent The time that was spent during the current phase in minutes (normally, a phase lasts 1 hour).
     */
    public void advanceAssemblyLine(int timeSpent) {
        boolean success = assemblyManager.advance(timeSpent);
        if (!success) ui.showErrorMessage("Assembly line is blocked!");

        var ordersOnAssembly = assemblyManager.getOrdersOnAssemblyLine();
        Map<String, String> currentOrdersOnAssemblyLine = convertToStringMap(ordersOnAssembly);
        ui.showAssemblyLineStatusAfterMove(currentOrdersOnAssemblyLine);
    }
}
