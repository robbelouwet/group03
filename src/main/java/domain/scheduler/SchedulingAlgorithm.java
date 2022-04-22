package domain.scheduler;

import domain.order.CarOrder;

import java.util.List;

public interface SchedulingAlgorithm {
    List<CarOrder> getOrderedList(List<CarOrder> carOrders);
}
