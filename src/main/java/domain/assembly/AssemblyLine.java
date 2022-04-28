package domain.assembly;

import domain.order.CarOrder;
import domain.order.OrderStatus;
import domain.scheduler.ProductionScheduler;
import domain.scheduler.TimeManager;
import lombok.Getter;
import persistence.CarOrderRepository;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
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
    private final TimeManager timeManager;


    /**
     * @param workStations The workstations that the {@code AssemblyLine} will contain, in the form of a {@code LinkedList}.
     * @param scheduler    The {@code ProductionScheduler} who provides following car orders.
     */
    public AssemblyLine(LinkedList<WorkStation> workStations, ProductionScheduler scheduler, TimeManager timeManager) {
        this.scheduler = scheduler;
        this.timeManager = timeManager;
        scheduler.setCurrentOrdersOnAssemblyLine(new LinkedList<>(workStations.stream().map(WorkStation::getCarOrder).collect(Collectors.toList())));
        this.workStations = workStations;
        for (WorkStation ws : workStations) {
            WorkStationListener wsListener = timeSpent -> {
                timeManager.timePassedOnStep(timeSpent);
                if (hasAllCompleted()) {
                    timeManager.resetStep();
                    advance();
                }
                scheduler.recalculatePredictedEndTimes();
            };
            ws.addListener(wsListener);
        }
        scheduler.registerListener(order -> {
            if (hasAllCompleted()) {
                advance();
            } else {
                if (timeManager.getTimeSpentOnThisStep() == 0 && workStations.get(0).hasCompleted())
                    restartFirstWorkStation();
            }
        });
    }

    private void advance() {
        workStations.getLast().finishCarOrder(timeManager.getCurrentTime());

        moveAllOrders();
        resetAllTasksOfWorkStations();
        restartFirstWorkStation();

        // Notify the scheduler about our new state
        var orders2 = workStations.stream().map(WorkStation::getCarOrder).collect(Collectors.toList());
        Collections.reverse(orders2);
        scheduler.setCurrentOrdersOnAssemblyLine(new LinkedList<>(orders2));
    }

    private void resetAllTasksOfWorkStations() {
        workStations.forEach(WorkStation::resetAllTasks);
    }

    private void restartFirstWorkStation() {
        WorkStation first = workStations.getFirst();
        var nextOrder = scheduler.getNextOrder();
        if (nextOrder != null) {
            nextOrder.setStatus(OrderStatus.OnAssemblyLine);
            first.updateCurrentOrder(nextOrder);
            nextOrder.setStartTime(timeManager.getCurrentTime());
        }
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
}
