package services;

import app.utils.ConsoleReader;
import domain.car.options.Option;
import domain.car.options.OptionCategory;
import domain.order.CarOrder;
import domain.scheduler.DateTime;
import domain.scheduler.ProductionScheduler;
import domain.scheduler.SchedulingAlgorithm;
import domain.scheduler.TimeManager;
import persistence.CarOrderRepository;
import persistence.DataSeeder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class ProductionSchedulerManager {
    private final ProductionScheduler productionScheduler;
    private TimeManager timeManager;

    public ProductionSchedulerManager(ProductionScheduler productionScheduler, TimeManager timeManager) {
        this.productionScheduler = productionScheduler;
        this.timeManager = timeManager;
    }

    /**
     * New algorithm has been chosen. Needs to be altered in the system.
     *
     * @param algorithm       Textual representation of the algorithm.
     * @param selectedOptions Optional of selectedOptions. Some algorithms need to know which selected
     *                        Car Options need priority.
     * @return boolean whether the algorithm has been changed succesfully.
     */
    public boolean selectAlgorithm(String algorithm, Optional<Map<String, String>> selectedOptions) {
        var selectedAlgorithm = DataSeeder.getSchedulingAlgorithms().get(algorithm);
        SchedulingAlgorithm schedulingAlgorithm;
        try {
            Class<?> clazz = Class.forName(selectedAlgorithm);
            if (selectedOptions.isPresent()) {
                // Specification-Batch
                Constructor<?> ctor = clazz.getConstructor(Map.class);
                schedulingAlgorithm = (SchedulingAlgorithm) ctor.newInstance(selectedOptions.get());
            } else {
                // FIFO
                Constructor<?> ctor = clazz.getConstructor();
                schedulingAlgorithm = (SchedulingAlgorithm) ctor.newInstance();
            }
            return productionScheduler.switchAlgorithm(schedulingAlgorithm);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            ConsoleReader.getInstance().println("Something went wrong with selecting the algorithm!");
            return false;
        }
    }

    /**
     * @return list of scheduling algorithms that are available in the system.
     */
    public List<String> getSchedulingAlgorithms() {
        return new ArrayList<>(DataSeeder.getSchedulingAlgorithms().keySet());
    }

    /**
     * Method for that calculates the possible car options to give priority to.
     * Checks if the unique key-value pair combinations is present more than 2 times, if so:
     * then that unique combination is a possible combination to prioritize.
     *
     * @return all the possible combinations that fulfill the constraint.
     */
    public List<Map<OptionCategory, Option>> getPossibleOrdersForSpecificationBatch() {
        var optionsList = productionScheduler
                .getOrderedListOfPendingOrders()
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

    public Map<String, String> getStatistics() {
        var map = new HashMap<String, String>();

        var orders = CarOrderRepository.getInstance().getOrders();

        // beware, this is JodaTime, not java's DateTime!
        DateTime now = timeManager.getCurrentTime();

        var last2days = orders.stream().filter(o -> o.getEndTime().getDays() > now.getDays() - 2).toList();
        var yesterday = orders.stream().filter(o -> o.getEndTime().getDays() > now.getDays() - 1).toList();

        var _2DaysAgo = last2days.stream().filter(o -> !yesterday.contains(o)).toList();

        map.put("# orders finished day before yesterday", Integer.toString(_2DaysAgo.size()));
        map.put("# orders finished yesterday", Integer.toString(yesterday.size()));
        map.put("average amount of orders finished per day", Integer.toString((_2DaysAgo.size() + yesterday.size()) / 2));

        // map to their delays, and sort delays on date of occurrence
        var delaysInMinutes = orders.stream().sorted((o1, o2) ->
                (int) (o1.getEndTime().getMinutes() - o2.getEndTime().getMinutes())
        ).map(o -> o.getOrderTime().subtractTime(o.getEndTime().getMinutes()).getMinutes()).toList();

        var sumDelays = delaysInMinutes.stream().reduce(0L, (acc, e) -> acc += e);

        map.put("Average delay", Long.toString(sumDelays / delaysInMinutes.size()));
        map.put("last 2 delays", String.format("%l %l"))


        return map;
    }
}
