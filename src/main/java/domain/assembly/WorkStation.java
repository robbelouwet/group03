package domain.assembly;

import domain.order.CarOrder;
import domain.order.OrderStatus;
import domain.time.DateTime;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class WorkStation {
    @Getter
    private String name;
    private CarOrder currentOrder;
    private List<AssemblyTask> tasks;


    public List<AssemblyTask> getTasks() {
        return tasks.stream().filter(t -> !t.isFinished()).collect(Collectors.toList());
    }

    public WorkStation(String name, List<AssemblyTask> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    private WorkStation(String name, List<AssemblyTask> tasks, CarOrder currentOrder) {
        this.name = name;
        if (currentOrder != null) {
            this.currentOrder = currentOrder.copy();
        } else {
            this.currentOrder = null;
        }
        this.tasks = tasks.stream().map(AssemblyTask::copy).collect(Collectors.toList());
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

    public WorkStation copy() {
        return new WorkStation(name, tasks, currentOrder);
    }

    @Override
    public String toString() {
        return "Workstation: [" + this.getName() + "]";
    }
}