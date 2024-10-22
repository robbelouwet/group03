package services;

import domain.car.options.Option;
import domain.car.options.OptionCategory;
import domain.order.CarOrder;
import domain.scheduler.*;
import persistence.CarOrderRepository;
import persistence.DataSeeder;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.reverse;
import static java.util.stream.Collectors.groupingBy;
import static math.Math.average;
import static math.Math.median;

public class ProductionSchedulerManager {
    private final ProductionScheduler productionScheduler;
    private final TimeManager timeManager;
    private final CarOrderRepository carOrderRepository;
    private final AlgorithmDirector director;

    public ProductionSchedulerManager(ProductionScheduler productionScheduler, TimeManager timeManager, CarOrderRepository carOrderRepository) {
        this.productionScheduler = productionScheduler;
        this.timeManager = timeManager;
        this.carOrderRepository = carOrderRepository;
        this.director = new AlgorithmDirector();
    }

    /**
     * New algorithm has been chosen. Needs to be altered in the system.
     *
     * @param algorithm Textual representation of the algorithm.
     * @param options   wrapper class to store data/fields for algorithm construction.
     *                  Almost all algorithms need different parameters.
     * @return boolean whether the algorithm has been changed succesfully.
     */
    public boolean selectAlgorithm(String algorithm, AlgorithmOptions options) {
        if (DataSeeder.getSchedulingAlgorithms().containsKey(algorithm)) {
            var algorithmBuilder = DataSeeder.getSchedulingAlgorithms().get(algorithm);
            var builtAlgorithm = director.buildAlgorithm(algorithmBuilder, options);
            productionScheduler.switchAlgorithm(builtAlgorithm);
            return true;
        }
        return false;
    }

    /**
     * @return list of scheduling algorithms that are available in the system.
     */
    public List<String> getSchedulingAlgorithms() {
        return new ArrayList<>(DataSeeder.getSchedulingAlgorithms().keySet());
    }

    /**
     * Method for that calculates the possible car selectedOptions to give priority to.
     * Checks if the unique key-value pair combinations is present more than 2 times, if so:
     * then that unique combination is a possible combination to prioritize.
     *
     * @return all the possible combinations that fulfill the constraint.
     */
    public List<Map<OptionCategory, Option>> getPossibleOrdersForSpecificationBatch() {
        var optionsList = productionScheduler
                .getPendingOrders()
                .stream()
                .map(CarOrder::getSelections)
                .toList();

        return optionsList.stream()
                .filter(o -> Collections.frequency(optionsList, o) >= 3)
                .distinct()
                .toList();
    }

    /**
     * @return the currently selected scheduling algorithm.
     */
    public SchedulingAlgorithm getCurrentAlgorithm() {
        return productionScheduler.getSchedulingAlgorithm();
    }

    public Statistics getStatistics() {
        var orders = carOrderRepository.getOrders();
        DateTime now = timeManager.getCurrentTime();

        // amount of car orders finished, grouped per day
        var ordersGroupedPerDay = orders.stream().filter(CarOrder::isFinished).collect(groupingBy(o -> o.getStartTime().getDays()));
        // a sorted list of the amounts only, still grouped per day
        var amountsGroupedPerDay = ordersGroupedPerDay.values().stream().map(List::size).sorted().toList();

        // average and median finished per day
        var averageFinishedPerDay = average(amountsGroupedPerDay.stream().map(Integer::longValue).toList());
        var medianFinishedPerDay = median(amountsGroupedPerDay.stream().map(Integer::longValue).toList());

        // amount finished yesterday and day before
        var ordersFinishedYesterday = ordersGroupedPerDay.getOrDefault(now.getDays() - 1, new ArrayList<>()).size();

        var ordersFinishedDayBefore = ordersGroupedPerDay.getOrDefault(now.getDays() - 2, new ArrayList<>()).size();

        // every order mapped to its delay in minutes
        var delaysInMinutes = orders.stream().filter(CarOrder::isFinished).map(co -> co.getStartTime().subtractTime(co.getOrderTime()).getMinutes()).collect(Collectors.toList());

        // Average and median delay
        var averageDelay = average(delaysInMinutes);
        float medianDelay = median(delaysInMinutes);

        // last 2 delayed orders
        var sortedDelays = orders.stream().filter(CarOrder::isFinished).sorted(Comparator.comparing(CarOrder::getStartTime)).collect(Collectors.toList());
        reverse(sortedDelays);
        var lastDelayDate = sortedDelays.size() >= 1 ? sortedDelays.get(0).getStartTime() : null;
        Long lastDelay = sortedDelays.size() >= 1
                ? sortedDelays.get(0).getStartTime().subtractTime(sortedDelays.get(0).getOrderTime()).getMinutes()
                : null;

        var secondToLastDelayDate = sortedDelays.size() >= 2 ? sortedDelays.get(1).getStartTime() : null;
        Long secondToLastDelay = sortedDelays.size() >= 2
                ? sortedDelays.get(1).getStartTime().subtractTime(sortedDelays.get(1).getOrderTime()).getMinutes()
                : null;

        return new Statistics(lastDelay, lastDelayDate, secondToLastDelay, secondToLastDelayDate, medianDelay, averageDelay,
                ordersFinishedYesterday, ordersFinishedDayBefore, medianFinishedPerDay, averageFinishedPerDay);
    }
}
