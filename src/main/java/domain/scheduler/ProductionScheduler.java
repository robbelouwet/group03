package domain.scheduler;

import domain.order.OrderStatus;
import domain.order.CarOrder;
import domain.time.DateTime;
import domain.time.TimeManager;
import lombok.Getter;
import persistence.CarOrderCatalog;
import persistence.CarOrderCatalogObserver;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProductionScheduler implements CarOrderCatalogObserver {
    @Getter
    private static final ProductionScheduler instance = new ProductionScheduler();
    private static final long START_SHIFT = 6 * 60;
    private static final long END_SHIFT = 22 * 60;
    private static final long DEFAULT_PRODUCTION_TIME = 3 * 60;

    private final CarOrderCatalog carOrderCatalog;
    private boolean firstSpotFree = true; // TODO not a fan of this, we need to think of a better solution

    private ProductionScheduler() {
        carOrderCatalog = CarOrderCatalog.getInstance();
        carOrderCatalog.registerListener(this);
    }

    private List<CarOrder> getOrderedListOfPendingOrders() {
        return carOrderCatalog.getOrders().stream().filter(o -> o.getStatus().equals(OrderStatus.Pending)).sorted(Comparator.comparing(CarOrder::getStartTime)).collect(Collectors.toList());
    }

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

    private DateTime getFirstFinishTimeNextDay(DateTime time) {
        return new DateTime(time.getDays() + 1, (START_SHIFT + DEFAULT_PRODUCTION_TIME) / 60, (START_SHIFT + DEFAULT_PRODUCTION_TIME) % 60);
    }

    private DateTime calculatePredictedTimeBasedOnPreviousTime(DateTime time) {
        // Check if there is time to finish the order on the same day
        if (time.getMinutesInDay() + 60 <= END_SHIFT) {
            return time.addTime(60);
        }
        // Else we start the day after
        return getFirstFinishTimeNextDay(time);
    }

    private DateTime calculateEndTimeOfFirstOrder() {
        var currentTime = TimeManager.getCurrentTime();

        // TODO rethink this, because if there is an order on the first spot, we need to add 60 otherwise we don't

        // Is there time to put another order on the belt in 60 minutes?
        if (currentTime.getMinutes() + (firstSpotFree ? 0 : 60) + DEFAULT_PRODUCTION_TIME <= END_SHIFT) {
            return currentTime.addTime((firstSpotFree ? 0 : 60) + DEFAULT_PRODUCTION_TIME);
        }
        return getFirstFinishTimeNextDay(currentTime);
    }

    /**
     * Notify the scheduler that another order was added and calculate its predicted end time
     *
     * @param order the new order
     */
    @Override
    public void carOrderAdded(CarOrder order) {
        var orders = getOrderedListOfPendingOrders();
        if (orders.size() > 1) {
            // The last == order, so we take the one before that
            var lastPendingOrder = orders.get(orders.size() - 2);
            order.setEndTime(calculatePredictedTimeBasedOnPreviousTime(lastPendingOrder.getEndTime()));
        } else {
            order.setEndTime(calculateEndTimeOfFirstOrder());
        }
    }

    private void recalculatePredictedEndTimes() {
        var orders = getOrderedListOfPendingOrders();
        if (orders.size() > 0) {
            var previousOrderTime = calculateEndTimeOfFirstOrder();

            orders.get(0).setEndTime(previousOrderTime);
            // Calculate the next time based on the previous order time
            for (var order : orders.stream().skip(1).collect(Collectors.toList())) {
                previousOrderTime = calculatePredictedTimeBasedOnPreviousTime(previousOrderTime);
                order.setEndTime(previousOrderTime);
            }
        }
    }

    /**
     * Notify the scheduler that time has passed and the first spot on the assembly line is free.
     * If the passed time is not 60 minutes, it will recalculate the predicted end times
     *
     * @param minutes the passed time in minutes
     */
    public void timePassed(long minutes) {
        TimeManager.addTime(minutes);
        firstSpotFree = true;
        if (minutes != 60) {
            recalculatePredictedEndTimes();
        }
    }

    public void firstSpotTaken() {
        firstSpotFree = false;
    }
}