package domain.scheduler;

import domain.order.CarOrder;
import domain.order.OrderStatus;

import java.util.Comparator;
import java.util.List;

public interface SchedulingAlgorithm {

    default boolean isFinished() {
        return false;
    }

    default boolean readyToSwitch(){
        return true;
    };

    default List<CarOrder> getOrderedListOfPendingOrders(List<CarOrder> carOrders){
        return carOrders.stream()
                .filter(o -> o.getStatus().equals(OrderStatus.Pending))
                .filter(o -> o.getEndTime() != null)
                .sorted(Comparator.comparing(CarOrder::getEndTime).reversed())
                .toList();
    }

    CarOrder getNextOrder(List<CarOrder> carOrders);
}
