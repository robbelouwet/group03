package domain.assembly;

import domain.order.CarOrder;
import domain.order.OrderStatus;
import domain.time.DateTime;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class WorkStation {
    @Getter
    private String name;
    @Setter
    private CarOrder currentOrder;
    // TODO: assign
    private List<AssemblyTask> tasks;

    public List<AssemblyTask> getTasks() {
        return new ArrayList<>(tasks);
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
}