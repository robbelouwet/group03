package domain.assembly;

import domain.order.OrderStatus;
import domain.scheduler.ProductionScheduler;
import domain.time.TimeManager;
import lombok.Getter;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class AssemblyLine {
    private final LinkedList<WorkStation> workStations;
    @Getter
    private final ProductionScheduler scheduler;

    public AssemblyLine(LinkedList<WorkStation> workStations, ProductionScheduler scheduler) {
        this.scheduler = scheduler;
        this.workStations = workStations;
    }

    public boolean advance(int timeSpent) {
        if (!workStations.stream().allMatch(WorkStation::hasCompleted)) return false;
        scheduler.recalculatePredictedEndTimes(timeSpent);
        if (hasAllCompleted()) {
            finishLastWorkStation();
            moveAllOrders();
            restartFirstWorkStation();
        }

        return true;
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
        if (last.getCarOrder() != null)
            last.finishCarOrder();
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

    public LinkedList<WorkStation> getWorkStations() {
        return workStations;
    }

    public List<WorkStation> getAvailableWorkStations() {
        return workStations.stream().filter(WorkStation::hasCompleted).collect(Collectors.toList());
    }

    private boolean hasAllCompleted() {
        for (WorkStation ws : workStations) {
            if (!ws.hasCompleted()) return false;
        }
        return true;
    }
}
