package domain.scheduler;

import domain.order.CarOrder;
import domain.order.OrderStatus;
import lombok.Getter;
import persistence.CarOrderCatalogObserver;
import persistence.CarOrderRepository;

import java.util.*;
import java.util.stream.Collectors;

public class ProductionScheduler {
    private static final long START_SHIFT = 6 * 60;  // Day starts at 6 o' clock
    private static final long END_SHIFT = 22 * 60;  // Day ends at 22 o' clock
    private long overtime = 0;
    @Getter
    private SchedulingAlgorithm schedulingAlgorithm;

    private final CarOrderRepository carOrderRepository;

    private final List<ScheduledOrdersListener> listeners = new ArrayList<>();

    private final TimeManager timeManager;
    private LinkedList<CarOrder> currentOrdersOnAssemblyLine;

    public ProductionScheduler(CarOrderRepository carOrderRepository, TimeManager timeManager, SchedulingAlgorithm schedulingAlgorithm) {
        this(carOrderRepository, timeManager, new LinkedList<>(Arrays.asList(new CarOrder[]{null, null, null})), schedulingAlgorithm);
    }

    private ProductionScheduler(CarOrderRepository carOrderRepository, TimeManager timeManager, LinkedList<CarOrder> currentOrdersOnAssemblyLine, SchedulingAlgorithm schedulingAlgorithm) {
        this.timeManager = timeManager;
        this.currentOrdersOnAssemblyLine = currentOrdersOnAssemblyLine;
        this.carOrderRepository = carOrderRepository;
        this.schedulingAlgorithm = schedulingAlgorithm;
        CarOrderCatalogObserver orderCatalogObserver = order -> {
            order.setOrderTime(timeManager.getCurrentTime());
            recalculatePredictedEndTimes();
            for (var listener : listeners) {
                listener.orderScheduled(order);
            }
        };
        this.carOrderRepository.registerListener(orderCatalogObserver);
        recalculatePredictedEndTimes();  // Do this for the orders that are already in the repository
    }

    public List<CarOrder> getPendingOrders() {
        return carOrderRepository.getOrders().stream().filter(o -> o.getStatus().equals(OrderStatus.Pending)).collect(Collectors.toList());
    }

    private List<CarOrder> getOrderedListOfPendingOrders() {
        // return copy!
        return new ArrayList<>(schedulingAlgorithm.getOrderedListOfPendingOrders(getPendingOrders()));
    }

    public void setCurrentOrdersOnAssemblyLine(LinkedList<CarOrder> currentOrdersOnAssemblyLine) {
        this.currentOrdersOnAssemblyLine = currentOrdersOnAssemblyLine;
    }

    /**
     * Gets the next order to put on the assembly line
     * Also checks whether a new day has started
     *
     * @return The next order
     */
    public CarOrder getNextOrder() {
        var orders = getPendingOrders();
        if (schedulingAlgorithm.isFinished(orders)) schedulingAlgorithm = new FIFOSchedulingAlgorithm();
        var order = schedulingAlgorithm.getNextOrder(orders);
        // If this order will be finished the next day
        if (order != null && order.getEndTime().getDays() > timeManager.getCurrentTime().getDays()) {
            // If there are no orders on the assembly line, we can change to the next day and return this order
            if (currentOrdersOnAssemblyLine.stream().allMatch(Objects::isNull)) {
                overtime = timeManager.getCurrentTime().getMinutesInDay() - END_SHIFT;
                timeManager.nextDay();
            } else {  // If there are orders, we should wait till there are no orders on the assemblyline anymore
                return null;
            }
        }
        return order;
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
    private int simulateScheduling(DateTime currentTime, long overtime, long timeSpentOnThisStep, LinkedList<CarOrder> ordersOnAssemblyLine, LinkedList<CarOrder> pendingOrders) {
        if (pendingOrders.size() == 0 && ordersOnAssemblyLine.stream().allMatch(Objects::isNull))
            return 0;  // We have scheduled everything

        // When will this cycle be done?
        DateTime endTime = currentTime.addTime(-timeSpentOnThisStep + Math.max(getMinutesOnStep(ordersOnAssemblyLine), timeSpentOnThisStep));
        var orderFinished = ordersOnAssemblyLine.pop();  // Remove the order from the assembly line

        // If we are over the end of the day
        if (endTime.getMinutesInDay() > END_SHIFT - overtime || endTime.getMinutesInDay() < START_SHIFT) {
            if (orderFinished == null) { // If the first workstation is empty, and we are already over the deadline
                var toBackTrack = ordersOnAssemblyLine.size();
                while (ordersOnAssemblyLine.pop() == null) toBackTrack--;
                return toBackTrack;
            }
            if (orderFinished.getStatus().equals(OrderStatus.Pending))
                return ordersOnAssemblyLine.size() + 1;  // This is not a valid scheduling, and we need to backtrack the size of the assembly line
        }

        if (orderFinished != null) {
            orderFinished.setEndTime(endTime);  // Set the predicted end time
        }

        // Now we add another order on the line
        CarOrder nextOrderOnLine;
        try {
            nextOrderOnLine = pendingOrders.pop();
        } catch (NoSuchElementException e) {
            nextOrderOnLine = null;
        }

        // Now try to put the next order on the line
        ordersOnAssemblyLine.addLast(nextOrderOnLine);
        // Simulate the rest
        int backtrack = simulateScheduling(endTime, overtime, 0, new LinkedList<>(ordersOnAssemblyLine), new LinkedList<>(pendingOrders));
        if (backtrack == 0)
            return 0;  // We can just add the next order on the line, without any problems at the end of the day
        else if (backtrack == 1) {  // We could not add this order at the end of the assembly line. Wait till the next day
            // Remove the order from the assembly line
            ordersOnAssemblyLine.removeLast();
            ordersOnAssemblyLine.add(null);

            // We can first set the end times of the orders on the assembly line by simulating this first
            backtrack = simulateScheduling(endTime, overtime, 0, new LinkedList<>(ordersOnAssemblyLine), new LinkedList<>());
            if (backtrack != 0) return backtrack - 1;  // This is not really possible

            // Now we put the next order on an empty assembly line the next day, the overtime is now always 0
            currentTime = new DateTime(currentTime.getDays() + 1, START_SHIFT / 60, START_SHIFT % 60);
            backtrack = simulateScheduling(currentTime, 0, 0, new LinkedList<>(Arrays.asList(null, null, nextOrderOnLine)), new LinkedList<>(pendingOrders));
            if (backtrack == 0) return 0;
        }
        return backtrack - 1;
    }

    public void recalculatePredictedEndTimes() {
        var ordersAssembly = new LinkedList<>(currentOrdersOnAssemblyLine);  // We need to make a copy
        var pendingOrders = new LinkedList<>(getOrderedListOfPendingOrders());  // We need a linked list
        int backtrack = simulateScheduling(timeManager.getCurrentTime(), 0, timeManager.getTimeSpentOnThisStep(), ordersAssembly, pendingOrders);
        if (backtrack != 0) {
            LinkedList<CarOrder> head = new LinkedList<>();
            for (int i= 0; i < backtrack; i++) {
                head.add(null);
            }
            head.addAll(pendingOrders);
            simulateScheduling(timeManager.getCurrentTime(), overtime, timeManager.getTimeSpentOnThisStep(), ordersAssembly, head);
        }
    }

    public ProductionScheduler copy() {
        return new ProductionScheduler(carOrderRepository.copy(), timeManager, new LinkedList<>(currentOrdersOnAssemblyLine.stream().map(CarOrder::copy).toList()), schedulingAlgorithm);
    }

    /**
     * This method will create the subclass of the Strategy Pattern for the scheduling algorithm and reassign it
     * as the current selected algorithm. The algorithm can only be changed if the current one is finished
     * doing its job of scheduling the orders or if it is ready to switch. Some algorithms can be blocked once they're
     * activated.
     *
     * @param schedulingAlgorithm The new scheduling algorithm that replaces the old one.
     */
    public void switchAlgorithm(SchedulingAlgorithm schedulingAlgorithm) {
        this.schedulingAlgorithm = schedulingAlgorithm;
        recalculatePredictedEndTimes();
    }

    public void registerListener(ScheduledOrdersListener listener) {
        listeners.add(listener);
    }
}
