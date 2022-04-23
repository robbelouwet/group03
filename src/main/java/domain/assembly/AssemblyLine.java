package domain.assembly;

import domain.order.OrderStatus;
import domain.scheduler.ProductionScheduler;
import domain.scheduler.SchedulingAlgorithm;
import domain.scheduler.TimeManager;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

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
     * @param scheduler    The {@code ProductionScheduler} who provides following car orders.
     */
    public AssemblyLine(LinkedList<WorkStation> workStations, ProductionScheduler scheduler) {
        this.scheduler = scheduler;
        this.workStations = workStations;
    }

    /**
     * This method will move the {@code AssemblyLine} one step forward if it isn't blocked (all the workstations are free of work).
     * As a result, every {@code CarOrder} will be moved to the next {@code WorkStation} and place a new {@code CarOrder} on the {@code AssemblyLine}.
     *
     * @param timeSpent The time that was spent during the current phase in minutes (normally, a phase lasts 1 hour).
     * @param simulation When true, the {@code AssemblyLine} will simulate the advance, if not: it will move one step forward in the real-life application.
     * @return true if the {@code AssemblyLine} has been moved forward one step.
     */
    public boolean advance(int timeSpent, boolean simulation) {
        if (simulation){
            advance();
        } else {
            if (!workStations.stream().allMatch(WorkStation::hasCompleted)) return false;
            scheduler.recalculatePredictedEndTimes(timeSpent);
            if (hasAllCompleted()) {
                advance();
            }
        }
        return true;
    }

    private void advance(){
        resetAllTasksOfWorkStations();
        finishLastWorkStation();
        moveAllOrders();
        restartFirstWorkStation();
    }

    private void resetAllTasksOfWorkStations() {
        workStations.forEach(WorkStation::resetAllTasks);
    }

    private void restartFirstWorkStation() {
        WorkStation first = workStations.getFirst();
        var nextOrder = scheduler.getNextOrder();
        if (nextOrder != null) {
            scheduler.firstSpotTaken();
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
        var current = it.previous();
        while (it.hasPrevious()) {
            var previous = it.previous();
            current.updateCurrentOrder(previous.getCarOrder());
            current = previous;
        }
        workStations.getFirst().updateCurrentOrder(null);
    }

    /**
     * @return {@code LinkedList&#60;WorkStation&#62;} All workstations possessed by the {@code AssemblyLine}
     */
    public LinkedList<WorkStation> getWorkStations() {
        return new LinkedList<>(workStations);
    }

    /**
     * @return {@code LinkedList&#60;WorkStation&#62;} All workstations that are occupied with a car order and assembly tasks.
     */
    public List<WorkStation> getBusyWorkstations() {
        return workStations.stream().filter(ws -> !ws.hasCompleted()).collect(Collectors.toList());
    }

    private boolean hasAllCompleted() {
        for (WorkStation ws : workStations) {
            if (!ws.hasCompleted()) return false;
        }
        return true;
    }

    public AssemblyLine copy(){
        LinkedList<WorkStation> copyWorkStations = new LinkedList<>();
        workStations.forEach(w -> copyWorkStations.add(w.copy()));
        return new AssemblyLine(new LinkedList<>(copyWorkStations), scheduler.copy());
    }

    public void selectAlgorithm(String selectedAlgorithm, Optional<Map<String, String>> selectedOptions) {
        scheduler.switchAlgorithm(selectedAlgorithm, selectedOptions);
    }

    public List<Map<String, String>> getPossibleOrdersForSpecificationBatch() {
        return scheduler.getPossibleOrdersForSpecificationBatch();
    }

    public SchedulingAlgorithm getCurrentAlgorithm() {
        return scheduler.getCurrentAlgorithm();
    }
}
