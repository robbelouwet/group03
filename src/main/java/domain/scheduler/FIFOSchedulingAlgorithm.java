package domain.scheduler;

import domain.order.CarOrder;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FIFOSchedulingAlgorithm implements SchedulingAlgorithm {
    @Override
    public List<CarOrder> getOrderedList(List<CarOrder> carOrders) {
        return carOrders.stream()
                .sorted(Comparator.comparing(CarOrder::getStartTime))
                .collect(Collectors.toList());
    }
}
