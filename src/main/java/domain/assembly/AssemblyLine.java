package domain.assembly;

import domain.order.CarOrder;
import domain.order.OrderStatus;
import domain.scheduler.ProductionScheduler;
import domain.scheduler.TimeManager;
import lombok.Getter;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Class {@code AssemblyLine} contains multiple workstations and is responsible for them.
 * The workstations are stored as a {@code LinkedList} for optimal iteration since the {@code AssemblyLine} is responsible for moving the workload of the workstations.
 * This Class also contains a {@code ProductionScheduler} who schedules the next {@code CarOrder} for the {@code AssemblyLine}.
 */
public class AssemblyLine {
    private final LinkedList<WorkStation> workStations;
    @Getter
    private final ProductionScheduler scheduler;

    /**
     * @param workStations The workstations that the {@code AssemblyLine} will contain, in the form of a {@code LinkedList}.
     * @param scheduler The {@code ProductionScheduler} who provides following car orders.
     */
    public AssemblyLine(LinkedList<WorkStation> workStations, ProductionScheduler scheduler) {
        this.scheduler = scheduler;
        this.workStations = workStations;
    }

    public boolean advance(int timeSpent) {
        if (!workStations.stream().allMatch(WorkStation::hasCompleted)) return false;
    /**
     * This method will move the {@code AssemblyLine} one step forward if it isn't blocked (all the workstations are free of work).
     * As a result, every {@code CarOrder} will be moved to the next {@code WorkStation} and place a new {@code CarOrder} on the {@code AssemblyLine}.
     *
     * @param timeSpent The time that was spent during the current phase in minutes (normally, a phase lasts 1 hour).
     * @return true if the {@code AssemblyLine} has been moved forward one step.
     */
    public void advance(int timeSpent) {
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

    /**
     * @return {@code LinkedList&#60;WorkStation&#62;} All workstations possessed by the {@code AssemblyLine}
     */
    public LinkedList<WorkStation> getWorkStations() {
        return new LinkedList<>(workStations);
    }

    public List<WorkStation> getBusyWorkstations() {
        return workStations.stream().filter(ws -> !ws.hasCompleted()).collect(Collectors.toList());
    }

    private boolean hasAllCompleted() {
        for (WorkStation ws : workStations) {
            if (!ws.hasCompleted()) return false;
        }
        return true;
    }

    /**
     * @param o The {@code CarOrder} that needs to be checked if it is present in a specific {@code WorkStation}
     * @return true if the {@code CarOrder} is present in the {@code WorkStation}
     */
    public boolean isPresentInLastWorkstation(CarOrder o) {
        var order = getWorkStations().getLast().getCarOrder();
        if (order == null || o == null)
            return false;
        return order.equals(o);
    }
}
