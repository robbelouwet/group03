package domain.assembly;

import domain.order.CarOrder;
import domain.order.OrderStatus;
import domain.scheduler.DateTime;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

public class WorkStation {
    @Getter
    private final String name;
    @Setter
    private CarOrder currentOrder;
    // TODO: assign
    private final List<AssemblyTask> tasks;

    public List<AssemblyTask> getPendingTasks() {
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
        return currentOrder == null
                || tasks.stream().allMatch(AssemblyTask::isFinished);
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

    @Override
    public String toString() {
        return "Workstation: [" + this.getName() + "]";
    }
}