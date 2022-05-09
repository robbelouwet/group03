package domain.scheduler;

import domain.order.CarOrder;
import domain.order.OrderStatus;
import lombok.Getter;
import persistence.CarOrderRepositoryObserver;
import persistence.CarOrderRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        CarOrderRepositoryObserver orderCatalogObserver = order -> {
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

    private long getOverTimeMinutes(DateTime endTime, long previousOvertime) {
        if (endTime.getMinutesInDay() > END_SHIFT - previousOvertime)
            return endTime.getMinutesInDay() - (END_SHIFT - previousOvertime);
        else if (endTime.getMinutesInDay() < START_SHIFT)
            return 24 * 60 - (END_SHIFT - previousOvertime) + endTime.getMinutesInDay();
        return 0;
    }

    private void setOvertime() {
        overtime = getOverTimeMinutes(timeManager.getCurrentTime(), overtime);
    }

    /**
     * Gets the next order to put on the assembly line
     * Also checks whether a new day has started
     *
     * @return The next order
     */
    public CarOrder getNextOrder() {
        var orders = getPendingOrders();
        // What if scheduling algorithm ends and needs to change to another one?
        schedulingAlgorithm = schedulingAlgorithm.nextAlgorithm(orders);
        var order = schedulingAlgorithm.getNextOrder(orders);
        // If this order will be finished the next day
        if (order != null && order.getEndTime().getDays() > timeManager.getCurrentTime().getDays()) {
            // If there are no orders on the assembly line, we can change to the next day and return this order
            if (currentOrdersOnAssemblyLine.stream().allMatch(o -> o == null || o.isFinished())) {
                setOvertime();
                timeManager.nextDay();
            } else {  // If there are orders, we should wait till there are no orders on the assemblyline anymore
                return null;
            }
        }
        return order;
    }

    private class Simulator {
        private void simulate() {
            var ordersOnAssemblyLine = new LinkedList<>(currentOrdersOnAssemblyLine);
            var pendingOrders = new LinkedList<>(getOrderedListOfPendingOrders());

            new StepSimulator(timeManager.getCurrentTime(), overtime, timeManager.getTimeSpentOnThisStep(), ordersOnAssemblyLine, pendingOrders);
        }

        private class StepSimulator {
            private final DateTime endTime;
            private final long previousOvertime;
            private long overtime = 0;

            private final LinkedList<CarOrder> ordersOnAssemblyLine;
            private final LinkedList<CarOrder> pendingOrders;

            //private CarOrder finishedOrder;

            private int backtrack = 0;

            public StepSimulator(DateTime startTime, long previousOvertime, List<CarOrder> ordersOnAssemblyLine, List<CarOrder> pendingOrders) {
                this(startTime, previousOvertime, 0, ordersOnAssemblyLine, pendingOrders);
            }

            public StepSimulator(DateTime startTime, long previousOvertime, long timeSpentOnThisStep, List<CarOrder> ordersOnAssemblyLine, List<CarOrder> pendingOrders) {
                this.previousOvertime = previousOvertime;
                this.ordersOnAssemblyLine = new LinkedList<>(ordersOnAssemblyLine);
                this.pendingOrders = new LinkedList<>(pendingOrders);

                endTime = startTime.addTime(Math.max(getExpectedMinutesOnStep() - timeSpentOnThisStep, timeSpentOnThisStep));

                simulate();
            }

            private boolean finished() {
                return pendingOrders.size() == 0 && ordersOnAssemblyLine.stream().allMatch(Objects::isNull);
            }

            private long getExpectedMinutesOnStep() {
                long minutes = 0;
                for (var order : ordersOnAssemblyLine) {
                    if (order != null) {
                        minutes = Math.max(minutes, order.getExpectedMinutesPerWorkStation());
                    }
                }
                return minutes;
            }

            private boolean afterHours() {
                return endTime.getMinutesInDay() > END_SHIFT - previousOvertime || endTime.getMinutesInDay() < START_SHIFT;
            }

            private long getOvertimeMinutes() {
                return getOverTimeMinutes(endTime, previousOvertime);
            }

            private DateTime startNextDay() {
                if (endTime.getMinutesInDay() < START_SHIFT) {
                    return new DateTime(endTime.getDays(), 0, START_SHIFT);
                }
                return new DateTime(endTime.getDays() + 1, 0, START_SHIFT);
            }

            private void getOrderOffBelt() {
                var finishedOrder = ordersOnAssemblyLine.pop();  // Remove the order from the assembly line

                if (afterHours()) {
                    if (finishedOrder != null && finishedOrder.getStatus().equals(OrderStatus.Pending)) {  // This is an order that is not yet on the assemblyline, so we can schedule it at another time
                        backtrack = ordersOnAssemblyLine.size() + 1;
                        return;
                    }
                }

                if (finishedOrder != null) {
                    finishedOrder.setEndTime(endTime);  // Set the predicted end time
                }
            }

            private StepSimulator putNextOrderOnBeltToday() {
                var nextPendingOrders = new LinkedList<>(pendingOrders);

                CarOrder nextOrderOnLine;
                try {
                    nextOrderOnLine = nextPendingOrders.pop();
                } catch (NoSuchElementException e) {
                    nextOrderOnLine = null;
                }

                // Now put the next order on the line
                var nextOrdersOnAssemblyLine = new LinkedList<>(ordersOnAssemblyLine);
                nextOrdersOnAssemblyLine.addLast(nextOrderOnLine);

                // And simulate it
                return new StepSimulator(endTime, previousOvertime, nextOrdersOnAssemblyLine, nextPendingOrders);
            }

            private void finishSimulationToday() {
                var simulationCurrentDay = new StepSimulator(endTime, previousOvertime, ordersOnAssemblyLine, new LinkedList<>());

                // If this fails, the predicted time is longer than a whole day for a single order
                if (!simulationCurrentDay.isValid())
                    throw new RuntimeException("You've added an order that takes longer than a single day to make. We can't schedule it");
                overtime = simulationCurrentDay.overtime;
            }

            private void simulateNextDay() {
                var nextDay = startNextDay();
                var emptyLine = IntStream.range(0, ordersOnAssemblyLine.size() + 1).mapToObj(o -> (CarOrder) null).toList();

                var simulationNextDay = new StepSimulator(nextDay, overtime, emptyLine, pendingOrders);

                if (!simulationNextDay.isValid()) {
                    // Rare case that the overtime is too much for a single order to be finished in a day
                    simulationNextDay = new StepSimulator(nextDay.addTime(60 * 24), 0, emptyLine, pendingOrders);
                    if (!simulationNextDay.isValid())
                        throw new RuntimeException("You've added an order that takes longer than a single day to make. We can't schedule it");
                }
            }

            private void simulate() {
                if (finished()) {
                    overtime = getOvertimeMinutes();
                    return;
                }

                getOrderOffBelt();
                if (!isValid()) return;  // Backtrack was set in subroutine getOrderOffBelt

                var simulation = putNextOrderOnBeltToday();
                if (simulation.isValid()) {
                    overtime = simulation.overtime;
                    return;
                } else if (simulation.backtrack == 1) {  // We could not add the order we just added. Wait till the next day
                    // Simulate orders that are on it already
                    finishSimulationToday();

                    simulateNextDay();
                    return;
                }
                backtrack = simulation.backtrack - 1;  // We couldn't put an order on the belt in a previous simulationstep
            }

            private boolean isValid() {
                return backtrack == 0;
            }
        }
    }

    public void recalculatePredictedEndTimes() {
        new Simulator().simulate();
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
