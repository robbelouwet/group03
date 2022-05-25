package domain.scheduler;

import domain.car.options.Option;
import domain.car.options.OptionCategory;
import domain.order.CarOrder;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpecificationBatchSchedulingAlgorithm implements SchedulingAlgorithm {
    @Setter
    private Map<OptionCategory, Option> selectedOptions;

    /*
      The Specification-Batch Algorithm is only finished when there are
      no pending orders in remainingOrders any longer.
     */
    private boolean isFinished(List<CarOrder> pendingOrders) {
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
    public SchedulingAlgorithm nextAlgorithm(List<CarOrder> pendingOrders) {
        return isFinished(pendingOrders) ? new FIFOSchedulingAlgorithm() : this;
    }

    @Override
    public String toString() {
        return "SB";
    }
}
