package domain.scheduler;

import domain.order.CarOrder;

import java.util.Comparator;
import java.util.List;

public class FIFOSchedulingAlgorithm implements SchedulingAlgorithm {
    @Override
    public CarOrder getNextOrder(List<CarOrder> carOrders) {
        return carOrders.stream()
                // has the same effect as saying getFirst()
                .min(Comparator.comparing(CarOrder::getStartTime))
                .orElse(null);
    }

    @Override
    public String toString() {
        return "FIFO";
    }
}
