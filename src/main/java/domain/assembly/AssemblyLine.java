package domain.assembly;

import domain.WorkStation;
import domain.scheduler.ProductionScheduler;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class AssemblyLine {
    // TODO: init the workstations
    private LinkedList<WorkStation> workStations;
    private final ProductionScheduler scheduler = ProductionScheduler.getInstance();

    public void advance(int timeSpent) {
        if (hasAllCompleted()) {
            finishLastWorkStation(timeSpent);
            moveAllOrders();
            restartFirstWorkStation();
        } else {
            // TODO: reschedule
        }
    }

    private void restartFirstWorkStation() {
        WorkStation first = workStations.getFirst();
        first.updateCurrentOrder(scheduler.getNextOrder());
    }

    private void finishLastWorkStation(int timeSpent){
        WorkStation last = workStations.getLast();
        last.updateEndTimeOrder(timeSpent);
        scheduler.updateSchedule(last.finishCarOrder());
    }

    /*
    General idea:
        1. Go through workstations in reverse order
        2. Move Car Order to next workstation (previous in iterator)
        3. Reset the pointer (because we have gone 1 too far)
     */
    private void moveAllOrders() {
        int size = workStations.size();
        ListIterator<WorkStation> it = workStations.listIterator(size);
        while (it.hasPrevious()) {
            var current = it.previous();
            if (it.hasPrevious()){
                var previous = it.previous();
                current.updateCurrentOrder(previous.getCarOrder());
                // Reset the pointer
                it.next();
            }
        }
    }

    public List<WorkStation> getAvailableWorkStations() {
        throw new UnsupportedOperationException();
    }

    private boolean hasAllCompleted() {
        for (WorkStation ws : workStations) {
            if (!ws.hasCompleted()) return false;
        }
        return true;
    }
}
