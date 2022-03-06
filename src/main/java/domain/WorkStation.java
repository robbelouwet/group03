package domain;

import java.util.ArrayList;

public class WorkStation {
    private AssemblyProcess currentProcess;

    public ArrayList<AssemblyTask> getTasks(){
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
