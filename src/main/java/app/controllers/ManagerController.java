package app.controllers;

import app.ui.interfaces.IManagerView;
import domain.assembly.AssemblyTask;
import domain.order.CarOrder;
import services.AssemblyManager;
import services.CarOrderManager;
import services.ManagerStore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class {@code ManagerController} is responsible for the communication between the UI and the Domain.
 */
public class ManagerController {
    private final AssemblyManager assemblyManager;
    private final CarOrderManager carOrderManager;
    private IManagerView ui;

    ManagerController(AssemblyManager assemblyManager, CarOrderManager carOrderManager) {
        this.assemblyManager = assemblyManager;
        this.carOrderManager = carOrderManager;
    }

    public void setUi(IManagerView ui) {
        this.ui = ui;
    }

    /**
     * Provides the UI with the pending & finished tasks and orders of the assembly line.
     */
    public void showMainMenu() {
        // TODO: pendingOrders -> onAssemblyLineOrders
        List<CarOrder> pendingOrders = carOrderManager.getPendingOrders();
        List<CarOrder> simFinishedOrders = assemblyManager.getSimulatedOrders(carOrderManager.getPendingOrders());
        Map<String, List<String>> pendingTasks = convertToStringList(assemblyManager.getPendingTasks());
        // TODO: finished tasks is not correct
        Map<String, List<String>> finishedTasks = convertToStringList(assemblyManager.getFinishedTasks());
        ui.showOverview(
                pendingOrders.stream().map(CarOrder::toString).collect(Collectors.toList()),
                simFinishedOrders.stream().map(CarOrder::toString).collect(Collectors.toList()),
                pendingTasks,
                finishedTasks
        );
    }

    /**
     * This method converts the Map<String, List<AssemblyTask>> to Map<String, List<String>>
     * This is because the view can not know of domain elements
     */
    private Map<String, List<String>> convertToStringList(Map<String, List<AssemblyTask>> pendingTasks) {
        Map<String, List<String>> tasks = new HashMap<>();
        pendingTasks.forEach((k, v) -> tasks.put(k, v.stream().map(AssemblyTask::toString).collect(Collectors.toList())));
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

        List<CarOrder> pendingOrders = carOrderManager.getPendingOrders();
        ui.showAssemblyLineStatusAfterMove(pendingOrders.stream().map(CarOrder::toString).collect(Collectors.toList()));
    }
}
