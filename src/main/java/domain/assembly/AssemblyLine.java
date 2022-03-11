package domain.assembly;

import domain.WorkStation;
import domain.scheduler.ProductionScheduler;

import java.util.LinkedList;
import java.util.List;

public class AssemblyLine {
    private LinkedList<WorkStation> workStations;
    private ProductionScheduler scheduler = ProductionScheduler.getInstance();

    public void advance() {
    }

    public List<WorkStation> getAvailableWorkStations() {
        throw new UnsupportedOperationException();
    }

    public void confirmMove(int timeSpent) {

    }

    private boolean hasAllCompleted() {
        throw new UnsupportedOperationException();
    }
}
