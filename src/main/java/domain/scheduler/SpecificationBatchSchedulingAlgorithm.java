package domain.scheduler;

import domain.order.CarOrder;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpecificationBatchSchedulingAlgorithm implements SchedulingAlgorithm {
    private final Map<String, String> selectedOptions;
    private List<CarOrder> remainingOrders;

    public SpecificationBatchSchedulingAlgorithm(Map<String, String> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    private void initialize(List<CarOrder> carOrders) {
        remainingOrders = carOrders.stream()
                .filter(o -> o.getSelections().equals(selectedOptions))
                .sorted(Comparator.comparing(CarOrder::getStartTime))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isFinished() {
        return remainingOrders != null && remainingOrders.size() == 0;
    }

    @Override
    public boolean readyToSwitch() {
        return isFinished();
    }

    @Override
    public CarOrder getNextOrder(List<CarOrder> carOrders) {
        if (remainingOrders == null) initialize(carOrders);
        if (!isFinished()) return remainingOrders.remove(0);
        else return null;
    }

    @Override
    public String toString() {
        return "SB";
    }
}
