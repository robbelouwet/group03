package domain.scheduler;

import domain.car.options.Option;
import domain.car.options.OptionCategory;
import domain.order.CarOrder;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpecificationBatchSchedulingAlgorithm implements SchedulingAlgorithm {
    private final Map<OptionCategory, Option> selectedOptions;

    public SpecificationBatchSchedulingAlgorithm(Map<OptionCategory, Option> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    @Override
    /*
      The Specification-Batch Algorithm is only finished when there are
      no pending orders in remainingOrders any longer.
     */
    public boolean isFinished(List<CarOrder> pendingOrders) {
        return pendingOrders.stream()
                .noneMatch(o -> o.getSelections().equals(selectedOptions));
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
    public String toString() {
        return "SB";
    }
}
