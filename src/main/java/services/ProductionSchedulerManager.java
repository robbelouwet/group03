package services;

import app.utils.ConsoleReader;
import domain.car.options.Option;
import domain.car.options.OptionCategory;
import domain.order.CarOrder;
import domain.scheduler.ProductionScheduler;
import domain.scheduler.SchedulingAlgorithm;
import persistence.DataSeeder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ProductionSchedulerManager {
    private final ProductionScheduler productionScheduler;

    public ProductionSchedulerManager(ProductionScheduler productionScheduler) {
        this.productionScheduler = productionScheduler;
    }

    /**
     * New algorithm has been chosen. Needs to be altered in the system.
     * @param algorithm Textual representation of the algorithm.
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
}
