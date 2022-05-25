package domain.scheduler;

import domain.car.options.Option;
import domain.car.options.OptionCategory;
import domain.order.CarOrder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpecificationBatchSchedulingAlgorithm implements SchedulingAlgorithm {
    private Map<OptionCategory, Option> selectedOptions;

    /*
      The Specification-Batch Algorithm is only finished when there are
      no pending orders in remainingOrders any longer.
     */
    private boolean isFinished(List<CarOrder> pendingOrders) {
        return pendingOrders.stream()
                .noneMatch(o -> o.getSelections().equals(selectedOptions));
    }

    /**
     * We need to validate the selectedOptions and make a copy without reference to the original one.
     * @param selectedOptions a Map of OptionCategory as key and an Option as value.
     *        This serves as the chosen options to prioritize the orders for the specification batch algorithm.
     * @throws IllegalArgumentException if selectedOptions is null, a key is null or an entry is null
     */
    public void setSelectedOptions(Map<OptionCategory, Option> selectedOptions){
        if (selectedOptions != null &&
                selectedOptions.keySet().stream().allMatch(Objects::nonNull) &&
                selectedOptions.entrySet().stream().allMatch(Objects::nonNull)){
            this.selectedOptions = new HashMap<>(selectedOptions);
        }
        else throw new IllegalArgumentException("The selected options are not valid!");
    }

    @Override
    public List<CarOrder> getOrderedListOfPendingOrders(List<CarOrder> carOrders) {
        return Stream.concat(
                carOrders.stream()
                        .filter(o -> o.getSelections().equals(selectedOptions))
                        .sorted(Comparator.comparing(CarOrder::getOrderTime)),
                carOrders.stream()
                        .filter(o -> !o.getSelections().equals(selectedOptions))
                        .sorted(Comparator.comparing(CarOrder::getOrderTime))
        ).collect(Collectors.toList());
    }

    @Override
    public SchedulingAlgorithm nextAlgorithm(List<CarOrder> pendingOrders) {
        return isFinished(pendingOrders) ? new FIFOSchedulingAlgorithm() : this;
    }

    @Override
    public String toString() {
        return "SB";
    }
}
