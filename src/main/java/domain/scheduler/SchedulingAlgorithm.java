package domain.scheduler;

import domain.order.CarOrder;

import java.util.List;

/**
 * This interface serves as the Strategy pattern for scheduling
 * the Car Orders so that the Production Scheduler is released of that task.
 */
public interface SchedulingAlgorithm {

    /**
     * Determines when the algorithm is completely done.
     * @return false if the algorithm still has pending orders.
     */
    boolean isFinished();

    /**
     * Determines when the algorithm can be altered.
     * This method is fundamentally different than the isFinished() method
     * because some algorithms can be altered even when they aren't finished yet.
     * @return true if the algorithm can be altered at any given time.
     */
    boolean readyToSwitch();

    /**
     * This method determines the order in which the pending orders should be processed.
     * Default implementation is ordered by FIFO principle.
     * @param carOrders The pending orders in the system
     * @return List<CarOrder> ordered list of CarOrders corresponding to the algorithm
     */
    List<CarOrder> getOrderedListOfPendingOrders(List<CarOrder> carOrders);

    /**
     * The most crucial method of the Strategy, which order should be processed next?
     * @param carOrders The pending orders in the system
     * @return The pending CarOrder that has the highest priority
     * to be processed according to the algorithm
     */
    CarOrder getNextOrder(List<CarOrder> carOrders);
}
