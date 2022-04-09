package domain.scheduler;

import domain.order.CarOrder;
import persistence.CarOrderRepository;
import persistence.CarOrderCatalogObserver;

import java.util.*;

public abstract class ProductionScheduler {
    private static final long START_SHIFT = 6 * 60;  // Day starts at 6 o' clock
    private static final long END_SHIFT = 22 * 60;  // Day ends at 22 o' clock

    private final CarOrderCatalogObserver orderCatalogObserver = new CarOrderCatalogObserver() {
        @Override
        public void carOrderAdded(CarOrder order) {
            order.setOrderTime(timeManager.getCurrentTime());
            recalculatePredictedEndTimes();
        }
    };

    final CarOrderRepository carOrderRepository;
    private long timeSpentThisCycle = 0;

    final TimeManager timeManager;
    LinkedList<CarOrder> currentOrdersOnAssemblyLine;

    ProductionScheduler(CarOrderRepository carOrderRepository, TimeManager timeManager, LinkedList<CarOrder> currentOrdersOnAssemblyLine) {
        this.timeManager = timeManager;
        this.currentOrdersOnAssemblyLine = currentOrdersOnAssemblyLine;
        this.carOrderRepository = carOrderRepository;
        this.carOrderRepository.registerListener(orderCatalogObserver);
        recalculatePredictedEndTimes();  // Do this for the orders that are already in the repository
    }

    public static ProductionScheduler of(ProductionScheduler oldScheduler, String newType) {
        return of(oldScheduler.carOrderRepository, oldScheduler.timeManager, oldScheduler.currentOrdersOnAssemblyLine, newType);
    }

    public static ProductionScheduler of(CarOrderRepository carOrderRepository, String type) {
        return of(carOrderRepository, new TimeManager(), new LinkedList<>(Arrays.asList(null, null, null)), type);
    }

    private static ProductionScheduler of(CarOrderRepository carOrderRepository, TimeManager timeManager, LinkedList<CarOrder> currentOrdersOnAssemblyLine, String newType) {
        // TODO implement other scheduler and change this factory method
        return switch (newType) {
            case "FIFO" -> new FIFOScheduler(carOrderRepository, timeManager, currentOrdersOnAssemblyLine);
            case "SB" -> new SpecificationBatchScheduler(carOrderRepository, timeManager, currentOrdersOnAssemblyLine);
            default -> throw new IllegalArgumentException(newType + "is not a valid scheduler");
        };
    }

    abstract List<CarOrder> getOrderedListOfPendingOrders();

    /**
     * @return the next order in line
     */
    public CarOrder getNextOrder() {
        var orders = getOrderedListOfPendingOrders();
        if (orders.size() == 0) {
            return null;
        }
        return orders.get(0);
    }

    /**
     * @param carOrders A list of orders and null values that are a snapshot of the orders on the assembly line
     * @return How long a step will take with these orders on the belt
     */
    private long getMinutesOnStep(List<CarOrder> carOrders) {
        long minutes = 0;
        for (var order : carOrders) {
            if (order != null) {
                minutes = Math.max(minutes, order.getExpectedMinutesPerWorkStation());
            }
        }
        return minutes;
    }

    /**
     * @param currentTime          The current time
     * @param timeSpentOnThisStep  The time that is already spent on the current cycle
     * @param ordersOnAssemblyLine An ordered list of orders and null values which is a snapshot of orders on the assembly line (first order is at the end of the line)
     * @param pendingOrders        A list of orders that need to be placed on the assembly line
     * @return The amount of steps you need to backtrack to get a valid scheduling. 0 means it is valid
     */
    private int simulateScheduling(DateTime currentTime, long timeSpentOnThisStep, LinkedList<CarOrder> ordersOnAssemblyLine, LinkedList<CarOrder> pendingOrders) {
        if (pendingOrders.size() == 0 && ordersOnAssemblyLine.stream().allMatch(Objects::isNull))
            return 0;  // We have scheduled everything

        // When will this cycle be done?
        DateTime endTime = currentTime.addTime(Math.max(getMinutesOnStep(ordersOnAssemblyLine) - timeSpentOnThisStep, timeSpentOnThisStep));
        if (endTime.getMinutesInDay() > END_SHIFT || endTime.getMinutesInDay() < START_SHIFT)
            return 3;  // This is not a valid scheduling, and we need to backtrack 3 steps

        // Now we try to simulate an advance of the assembly line
        var orderFinished = ordersOnAssemblyLine.pop();  // Remove the order from the assembly line
        if (orderFinished != null) {
            orderFinished.setEndTime(endTime);  // Set the predicted end time
        }

        CarOrder nextOrderOnLine;
        try {
            nextOrderOnLine = pendingOrders.pop();
        } catch (NoSuchElementException e) {
            nextOrderOnLine = null;
        }

        // Now try to put the next order on the line
        ordersOnAssemblyLine.addLast(nextOrderOnLine);
        // Simulate the rest
        int backtrack = simulateScheduling(endTime, 0, new LinkedList<>(ordersOnAssemblyLine), new LinkedList<>(pendingOrders));
        if (backtrack == 0)
            return 0;  // We can just add the next order on the line, without any problems at the end of the day
        else if (backtrack == 1) {  // We could not add this order at the end of the assembly line. Wait till the next day
            // Remove the order from the assembly line
            ordersOnAssemblyLine.removeLast();
            ordersOnAssemblyLine.add(null);

            // We can first set the end times of the orders on the assembly line by simulating this first
            backtrack = simulateScheduling(endTime, 0, new LinkedList<>(ordersOnAssemblyLine), new LinkedList<>());
            if (backtrack != 0) return backtrack - 1;  // This is not really possible

            // Now we put the next order on an empty assembly line the next day
            currentTime = new DateTime(currentTime.getDays() + 1, START_SHIFT / 60, START_SHIFT % 60);
            backtrack = simulateScheduling(currentTime, 0, new LinkedList<>(Arrays.asList(null, null, nextOrderOnLine)), new LinkedList<>(pendingOrders));
            if (backtrack == 0) return 0;
        }
        return backtrack - 1;
    }

    private void recalculatePredictedEndTimes() {
        var ordersAssembly = new LinkedList<>(currentOrdersOnAssemblyLine);  // We need to make a copy
        var pendingOrders = new LinkedList<>(getOrderedListOfPendingOrders());  // We need a linked list
        simulateScheduling(timeManager.getCurrentTime(), timeSpentThisCycle, ordersAssembly, pendingOrders);
    }

    /**
     * Notify the scheduler that we advanced the assembly line and how long we spent ong the last step
     *
     * @param minutes The minutes that were spent on the last step
     */
    public DateTime advanced(long minutes, LinkedList<CarOrder> ordersOnAssemblyLine) {
        timeManager.addTime(Math.max(timeSpentThisCycle, minutes));
        timeSpentThisCycle = 0;
        this.currentOrdersOnAssemblyLine = ordersOnAssemblyLine;
        recalculatePredictedEndTimes();
        return timeManager.getCurrentTime();
    }

    /**
     * Notify the scheduler that some time has passed, but we haven't advanced the assembly line yet
     *
     * @param minutes The amount of time that was spent on the last step
     */
    public void timePassed(long minutes) {
        timeSpentThisCycle = Math.max(timeSpentThisCycle, minutes);
        recalculatePredictedEndTimes();
    }

    public abstract ProductionScheduler copy();

    /**
     * Call this method if this scheduler is not active anymore
     */
    public void clean() {
        carOrderRepository.unregisterListener(orderCatalogObserver);
    }
}
