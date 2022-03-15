package domain.assembly;

import domain.order.CarOrder;
import domain.order.OrderStatus;
import domain.time.DateTime;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WorkStation {
    @Getter
    private String name;
    private CarOrder currentOrder;
    // TODO: assign
    private List<AssemblyTask> tasks;

    public static boolean isWorkstationName(String name, List<WorkStation> workStations) {
        return workStations.stream().anyMatch(ws -> ws.getName().equals(name));
    }

    public static WorkStation getWorkStationByName(String name, List<WorkStation> workStations) {
        return workStations.stream().filter(ws -> ws.getName().equals(name)).findAny().orElseThrow();
    }


    public List<AssemblyTask> getTasks() {
        return tasks.stream().filter(t -> !t.isFinished()).collect(Collectors.toList());
    }

    public WorkStation(String name, List<AssemblyTask> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    public CarOrder getCarOrder() {
        return currentOrder;
    }

    public boolean hasCompleted() {
        return currentOrder == null || currentOrder.isFinished();
    }

    public void updateEndTimeOrder(DateTime currentTime) {
        if (currentOrder != null)
            currentOrder.setEndTime(currentTime);
    }

    public void updateCurrentOrder(CarOrder order) {
        currentOrder = order;
    }

    public CarOrder finishCarOrder() {
        currentOrder.setStatus(OrderStatus.Finished);
        var order = currentOrder.copy();
        currentOrder = null;
        return order;
    }
}