package domain.scheduler;

import domain.order.CarOrder;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BatchSpecificationSchedulingAlgorithm implements SchedulingAlgorithm {
    private final Map<String, String> selectedOptions;

    public BatchSpecificationSchedulingAlgorithm(Map<String, String> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    @Override
    public List<CarOrder> getOrderedList(List<CarOrder> carOrders) {
        // TODO: determine how to sort different selections
        return carOrders.stream()
                .sorted((o1, a) -> {
                    if (o1.getSelections() == selectedOptions) {
                        return 0;
                    }
                    // Not the same set so must be processed after the orders with the same set of options
                    return 1;
                })
                .sorted(Comparator.comparing(CarOrder::getStartTime))
                .collect(Collectors.toList());
    }
}
