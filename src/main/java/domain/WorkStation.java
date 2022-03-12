package domain;

import domain.assembly.AssemblyTask;
import domain.order.CarOrder;

import java.time.LocalDateTime;
import java.util.List;

public class WorkStation {
    private CarOrder currentOrder;

    @Deprecated // currentProcess 1 -> 1..* tasks?
    public List<AssemblyTask> getTasks(){
        throw new UnsupportedOperationException();
    }

    public CarOrder getCarOrder(){
        throw new UnsupportedOperationException();
    }

    public boolean hasCompleted(){
        return currentOrder != null && currentOrder.isFinished();
    }

    public void updateEndTimeOrder(int timeSpent){
        LocalDateTime calculatedEndTime = currentOrder.getStartTime().plusMinutes(timeSpent);
        currentOrder.setEndTime(calculatedEndTime);
    }

    public void updateCurrentOrder(CarOrder order){}

    public CarOrder finishCarOrder(){
        currentOrder.setStatus(OrderStatus.Finished);
        var order = currentOrder.clone();
        currentOrder = null;
        return order;
    }
}
