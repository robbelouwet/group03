package domain.scheduler;

import domain.order.CarOrder;

import java.util.Comparator;
import java.util.List;

public class FIFOSchedulingAlgorithm implements SchedulingAlgorithm {
    @Override
    public List<CarOrder> getOrderedListOfPendingOrders(List<CarOrder> carOrders) {
        return carOrders.stream()
                .sorted(Comparator.comparing(CarOrder::getOrderTime))
                .toList();
    }

    @Override
    public CarOrder getNextOrder(List<CarOrder> carOrders) {
        var list = getOrderedListOfPendingOrders(carOrders);
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public SchedulingAlgorithm nextAlgorithm(List<CarOrder> pendingOrders) {
        return this;
    }

    @Override
    public String toString() {
        return "FIFO";
    }
}
