package domain.assembly;

import domain.WorkStation;
import domain.order.OrderStatus;
import domain.scheduler.ProductionScheduler;
import domain.time.TimeManager;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class AssemblyLine {
    // TODO: init the workstations
    private LinkedList<WorkStation> workStations;
    private final ProductionScheduler scheduler = ProductionScheduler.getInstance();

    public void advance(int timeSpent) {
        scheduler.timePassed(timeSpent);
        if (hasAllCompleted()) {
            finishLastWorkStation();
            moveAllOrders();
            restartFirstWorkStation();
        }
    }

    private void restartFirstWorkStation() {
        WorkStation first = workStations.getFirst();
        var nextOrder = scheduler.getNextOrder();
        if (nextOrder != null) {
            scheduler.firstSpotTaken();  // TODO rethink this, but we need some way for the scheduler to know if the first spot is free
            nextOrder.setStatus(OrderStatus.OnAssemblyLine);
            first.updateCurrentOrder(nextOrder);
        }
    }

    private void finishLastWorkStation() {
        WorkStation last = workStations.getLast();
        last.updateEndTimeOrder(TimeManager.getCurrentTime());
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
            if (it.hasPrevious()) {
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
