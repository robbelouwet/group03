package app.controllers;

import app.ui.interfaces.IManagerView;
import domain.assembly.AssemblyTask;
import domain.order.CarOrder;
import services.AssemblyManager;
import services.CarOrderManager;
import java.util.List;
import java.util.stream.Collectors;

public class ManagerController {
    private final AssemblyManager assemblyManager;
    private final CarOrderManager carOrderManager;
    private final IManagerView ui;

    public ManagerController(IManagerView ui) {
        this.ui = ui;
        assemblyManager = new AssemblyManager();
        carOrderManager = assemblyManager.getCarOrderManager();
    }

    public void showMainMenu() {
        List<CarOrder> pendingOrders = carOrderManager.getPendingOrders();
        // TODO: How to simulate the AL?
        List<CarOrder> simFinishedOrders = pendingOrders;
        List<List<AssemblyTask>> pendingTasks = assemblyManager.getPendingTasks();
        List<List<AssemblyTask>> finishedTasks = assemblyManager.getFinishedTasks();
        ui.showOverview(
                pendingOrders.stream().map(CarOrder::toString).collect(Collectors.toList()),
                simFinishedOrders.stream().map(CarOrder::toString).collect(Collectors.toList()),
                pendingTasks.stream().map(tasks -> tasks.stream().map(AssemblyTask::toString).collect(Collectors.toList())).collect(Collectors.toList()),
                finishedTasks.stream().map(tasks -> tasks.stream().map(AssemblyTask::toString).collect(Collectors.toList())).collect(Collectors.toList())
        );
    }

    public void advanceAssemblyLine(int timeSpent){
        boolean success = assemblyManager.advance(timeSpent);
        if (!success) ui.showErrorMessage("Assembly line is blocked!");

    }
}
