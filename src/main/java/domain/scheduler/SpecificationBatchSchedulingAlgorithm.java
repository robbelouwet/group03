package domain.scheduler;

import domain.car.options.Option;
import domain.car.options.OptionCategory;
import domain.order.CarOrder;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpecificationBatchSchedulingAlgorithm implements SchedulingAlgorithm {
    private final Map<OptionCategory, Option> selectedOptions;
    private int remainingOrders;

    public SpecificationBatchSchedulingAlgorithm(Map<OptionCategory, Option> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    /**
     * Once the Specification-Batch algorithm has been chosen it needs to keep track of the remaining
     * pending car orders that match the same properties (car options).
     *
     * @param carOrders List of pending car orders
     */
    private void initialize(List<CarOrder> carOrders) {
        remainingOrders = getOrderedListOfPendingOrders(carOrders).size();
    }

    @Override
    /*
      The Specification-Batch Algorithm is only finished when there are
      no pending orders in remainingOrders any longer.
     */
    public boolean isFinished() {
        return remainingOrders == 0;
    }

    @Override
    /*
        The Specification-Batch Algorithm is only ready to be altered by another algorithm
        if it is finished with the complete batch.
     */
    public boolean readyToSwitch() {
        return isFinished();
    }

    @Override
    public List<CarOrder> getOrderedListOfPendingOrders(List<CarOrder> carOrders) {
        return carOrders.stream()
                .filter(o -> o.getSelections().equals(selectedOptions))
                .sorted(Comparator.comparing(CarOrder::getStartTime))
                .collect(Collectors.toList());
    }

    @Override
    public CarOrder getNextOrder(List<CarOrder> carOrders) {
        // make sure there was no Car Order added after the batch initially was completely done.
        if (remainingOrders == 0) initialize(carOrders);
        return !isFinished() ? getOrderedListOfPendingOrders(carOrders).remove(0) : null;
    }

    @Override
    public String toString() {
        return "SB";
    }
}
