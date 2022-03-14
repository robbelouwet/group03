package domain.assembly;

import domain.order.CarOrder;
import domain.order.OrderStatus;
import domain.time.DateTime;

import java.util.List;

public class WorkStation {
    private CarOrder currentOrder;

    @Deprecated // currentProcess 1 -> 1..* tasks?
    public List<AssemblyTask> getTasks() {
        throw new UnsupportedOperationException();
    }

    public CarOrder getCarOrder() {
        throw new UnsupportedOperationException();
    }

    public boolean hasCompleted() {
        return currentOrder != null && currentOrder.isFinished();
    }

    public void updateEndTimeOrder(DateTime currentTime) {
        currentOrder.setEndTime(currentTime);
    }

    public void updateCurrentOrder(CarOrder order) {
    }

    public CarOrder finishCarOrder() {
        currentOrder.setStatus(OrderStatus.Finished);
        var order = currentOrder.copy();
        currentOrder = null;
        return order;
    }
}