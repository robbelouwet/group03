package domain.scheduler;

import domain.order.CarOrder;

import java.util.List;

/**
 * This interface serves as the Strategy pattern for scheduling
 * the Car Orders so that the Production Scheduler is released of that task.
 */
public interface SchedulingAlgorithm {

    /**
     * This method determines the order in which the pending orders should be processed.
     * Default implementation is ordered by FIFO principle.
     *
     * @param carOrders The pending orders in the system
     * @return List<CarOrder> ordered list of CarOrders corresponding to the algorithm
     */
    List<CarOrder> getOrderedListOfPendingOrders(List<CarOrder> carOrders);

    /**
     * The most crucial method of the Strategy, which order should be processed next?
     *
     * @param carOrders The pending orders in the system
     * @return The pending CarOrder that has the highest priority
     * to be processed according to the algorithm
     */
    default CarOrder getNextOrder(List<CarOrder> carOrders) {
        var orders = getOrderedListOfPendingOrders(carOrders);
        if (orders.size() == 0) return null;
        return orders.get(0);
    }

    /**
     * This method decides which instance of a Scheduling ALgorithm to change to when this instance is finished (no orders to process anymore)
     *
     * @param pendingOrders The pending orders in the system
     * @return The Scheduling Algorithm instance to change to
     */
    SchedulingAlgorithm nextAlgorithm(List<CarOrder> pendingOrders);
}
