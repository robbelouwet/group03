package domain.scheduler;

import app.utils.ConsoleReader;
import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.order.CarOrder;
import persistence.CarOrderCatalogObserver;
import persistence.CarOrderRepository;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.*;

public class ProductionScheduler implements CarOrderCatalogObserver {
    private static final long START_SHIFT = 6 * 60;  // Day starts at 6 o' clock
    private static final long END_SHIFT = 22 * 60;  // Day ends at 22 o' clock
    private static final long DEFAULT_PRODUCTION_TIME = 3 * 60;  // An order takes 3 hours normally
    private final CarOrderRepository carOrderRepository;
    private SchedulingAlgorithm schedulingAlgorithm;
    private boolean firstSpotFree = true;

    public ProductionScheduler(CarOrderRepository carOrderRepository, SchedulingAlgorithm schedulingAlgorithm) {
        this.carOrderRepository = carOrderRepository;
        this.carOrderRepository.registerListener(this);
        this.schedulingAlgorithm = schedulingAlgorithm;
        recalculatePredictedEndTimes();  // Do this for the orders that are already in the repository
        TimeManager.reset();
        /*
         * TODO: Remove everything below this block - pure testing
         */
        Map<String, List<String>> options = new HashMap<>();

        options.put("Body", List.of(new String[]{"sedan", "break"}));
        options.put("Color", List.of(new String[]{"red", "blue", "black", "white"}));
        options.put("Engine", List.of(new String[]{"standard", "performance"}));
        options.put("Gearbox", List.of(new String[]{"6 speed manual", "5 speed automatic"}));
        options.put("Seats", List.of(new String[]{"leather black", "leather white", "vinyl grey"}));
        options.put("Airco", List.of(new String[]{"manual", "automatic"}));
        options.put("Wheels", List.of(new String[]{"comfort", "sports"}));

        CarModelSpecification specification = new CarModelSpecification(options);
        List<CarOrder> testOrders = new ArrayList<CarOrder>(List.of(
                new CarOrder(
                        new DateTime(LocalDateTime.now().getMinute()),
                        new CarModel("Ford Fiesta", specification),
                        Map.of(
                                "Body", "sedan",
                                "Color", "red",
                                "Engine", "standard",
                                "Gearbox", "6 speed manual",
                                "Seats", "leather black",
                                "Airco", "manual",
                                "Wheels", "comfort"
                        )
                ),
                new CarOrder(
                        new DateTime(LocalDateTime.now().getMinute()),
                        new CarModel("Ford Fiesta", specification),
                        Map.of(
                                "Body", "sedan",
                                "Color", "red",
                                "Engine", "standard",
                                "Gearbox", "6 speed manual",
                                "Seats", "leather black",
                                "Airco", "manual",
                                "Wheels", "comfort"
                        )
                ),
                new CarOrder(
                        new DateTime(LocalDateTime.now().getMinute()),
                        new CarModel("Ford Fiesta", specification),
                        Map.of(
                                "Body", "sedan",
                                "Color", "red",
                                "Engine", "standard",
                                "Gearbox", "6 speed manual",
                                "Seats", "leather black",
                                "Airco", "manual",
                                "Wheels", "comfort"
                        )
                ),
                new CarOrder(
                        new DateTime(LocalDateTime.now().getMinute()),
                        new CarModel("Ford Fiesta", specification),
                        Map.of(
                                "Body", "sedan",
                                "Color", "black",
                                "Engine", "standard",
                                "Gearbox", "6 speed manual",
                                "Seats", "leather black",
                                "Airco", "manual",
                                "Wheels", "comfort"
                        )
                ),
                new CarOrder(
                        new DateTime(LocalDateTime.now().getMinute()),
                        new CarModel("Ford Fiesta", specification),
                        Map.of(
                                "Body", "sedan",
                                "Color", "black",
                                "Engine", "standard",
                                "Gearbox", "6 speed manual",
                                "Seats", "leather black",
                                "Airco", "manual",
                                "Wheels", "comfort"
                        )
                ),
                new CarOrder(
                        new DateTime(LocalDateTime.now().getMinute()),
                        new CarModel("Ford Fiesta", specification),
                        Map.of(
                                "Body", "sedan",
                                "Color", "black",
                                "Engine", "standard",
                                "Gearbox", "6 speed manual",
                                "Seats", "leather black",
                                "Airco", "manual",
                                "Wheels", "comfort"
                        )
                )
        ));
        for (CarOrder order : testOrders) {
            carOrderRepository.addOrder(order);
        }
    }

    private List<CarOrder> getOrderedListOfPendingOrders() {
        return schedulingAlgorithm.getOrderedListOfPendingOrders(
                carOrderRepository.getOrders()
        );
    }

    private CarOrder getLastScheduledOrder() {
        var orders = getOrderedListOfPendingOrders();
        if (orders.size() > 0) {
            return orders.get(orders.size() - 1);
        }
        return null;
    }

    /**
     * @return the next order in line according to the current scheduling algorithm.
     */
    public CarOrder getNextOrder() {
        var orders = carOrderRepository.getOrders();
        if (!schedulingAlgorithm.isFinished()) return schedulingAlgorithm.getNextOrder(orders);
        else schedulingAlgorithm = new FIFOSchedulingAlgorithm();
        return schedulingAlgorithm.getNextOrder(orders);
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
        var lastPendingOrder = getLastScheduledOrder();
        if (lastPendingOrder != null) {
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
            for (var order : orders.stream().skip(1).toList()) {
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
    public void recalculatePredictedEndTimes(long minutes) {
        TimeManager.addTime(minutes);
        firstSpotFree = true;
        if (minutes != 60) {
            recalculatePredictedEndTimes();
        }
    }

    public void firstSpotTaken() {
        firstSpotFree = false;
    }

    public ProductionScheduler copy() {
        return new ProductionScheduler(carOrderRepository.copy(), schedulingAlgorithm);
    }

    /**
     * This method will create the subclass of the Strategy Pattern for the scheduling algorithm and reassign it
     * as the current selected algorithm. The algorithm can only be changed if the current one is finished
     * doing its job of scheduling the orders or if it is ready to switch. Some algorithms can be blocked once they're
     * activated.
     *
     * @param selectedAlgorithm Textual representation of the scheduling algorithm
     * @param selectedOptions   Optional of selectedOptions. Some algorithms need to know which selected
     *                          Car Options need priority.
     *                          Will be Optional.empty() if the algorithm doesn't need this.
     */
    public void switchAlgorithm(String selectedAlgorithm, Optional<Map<String, String>> selectedOptions) {
        SchedulingAlgorithm algorithm;
        try {
            Class<?> clazz = Class.forName(selectedAlgorithm);
            if (selectedOptions.isPresent()) {
                // Specification-Batch
                Constructor<?> ctor = clazz.getConstructor(Map.class);
                algorithm = (SchedulingAlgorithm) ctor.newInstance(selectedOptions.get());
            } else {
                // FIFO
                Constructor<?> ctor = clazz.getConstructor();
                algorithm = (SchedulingAlgorithm) ctor.newInstance();
            }
            if (!schedulingAlgorithm.isFinished() && !schedulingAlgorithm.readyToSwitch())
                throw new IllegalStateException("The algorithm couldn't be changed because the current one hasn't finished yet!");
            schedulingAlgorithm = algorithm;
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            ConsoleReader.getInstance().println("Something went wrong with selecting the algorithm!");
        } catch (IllegalStateException e) {
            ConsoleReader.getInstance().println(e.getMessage());
        }
    }

    /**
     * Method for that calculates the possible car options to give priority to.
     * Checks if the unique key-value pair combinations is present more than 2 times, if so:
     * then that unique combination is a possible combination to prioritize.
     *
     * @return all the possible combinations that fulfill the constraint.
     */
    public List<Map<String, String>> getPossibleOrdersForSpecificationBatch() {
        var optionsList = getOrderedListOfPendingOrders()
                .stream()
                .map(CarOrder::getSelections)
                .toList();
        return optionsList.stream()
                .filter(o -> Collections.frequency(optionsList, o) >= 3)
                .distinct()
                .toList();
    }

    public SchedulingAlgorithm getCurrentAlgorithm() {
        return schedulingAlgorithm;
    }
}
