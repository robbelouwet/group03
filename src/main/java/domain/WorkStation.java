package domain;

import domain.assembly.AssemblyTask;
import domain.order.CarOrder;

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
        throw new UnsupportedOperationException();
    }

    public void updateEndTimeOrder(int timeSpent){}

    public void clearProcessOrder(){}

    public void updateCurrentOrder(CarOrder order){}
}
