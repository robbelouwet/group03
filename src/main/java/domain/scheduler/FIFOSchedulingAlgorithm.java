package domain.scheduler;

import domain.order.CarOrder;
import domain.order.OrderStatus;

import java.util.Comparator;
import java.util.List;

public class FIFOSchedulingAlgorithm implements SchedulingAlgorithm {
    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean readyToSwitch() {
        return true;
    }

    @Override
    public List<CarOrder> getOrderedListOfPendingOrders(List<CarOrder> carOrders) {
        return carOrders.stream()
                .filter(o -> o.getStatus().equals(OrderStatus.Pending))
                .filter(o -> o.getEndTime() != null)
                .sorted(Comparator.comparing(CarOrder::getEndTime).reversed())
                .toList();
    }

    @Override
    public CarOrder getNextOrder(List<CarOrder> carOrders) {
        var list = getOrderedListOfPendingOrders(carOrders);
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public String toString() {
        return "FIFO";
    }
}
