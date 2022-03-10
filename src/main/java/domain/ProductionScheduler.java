package domain;

import domain.assembly.AssemblyProcess;
import domain.car.CarOrder;
import lombok.Getter;
import services.CarOrderManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ProductionScheduler {
    @Getter
    private static final ProductionScheduler instance = new ProductionScheduler();

    private final Queue<AssemblyProcess> scheduledProcesses = new LinkedList<>();

    public void addOrder(CarOrder order){
        AssemblyProcess process = new AssemblyProcess(order);
        scheduledProcesses.add(process);
        // TODO calculate predicted end time
    }

    public AssemblyProcess getNextProcess(){
        throw new UnsupportedOperationException();
    }

    private void updateSchedule(AssemblyProcess process){
        // TODO how are we calculating this?
    }

    @Override
    public ProductionScheduler clone() {
        return new ProductionScheduler();
    }
}
