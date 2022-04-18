package domain.scheduler;

import domain.order.CarOrder;
import domain.order.OrderStatus;
import persistence.CarOrderRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SpecificationBatchScheduler extends ProductionScheduler {
    SpecificationBatchScheduler(CarOrderRepository carOrderRepository, TimeManager timeManager, LinkedList<CarOrder> currentOrdersOnAssemblyLine) {
        super(carOrderRepository, timeManager, currentOrdersOnAssemblyLine);
    }

    @Override
    List<CarOrder> getOrderedListOfPendingOrders() {
        // TODO implement this, but we need to implement options first
        // This is just a mock implementation, which is first in, last out
        var orders = carOrderRepository.getOrders().stream().filter(o -> o.getStatus().equals(OrderStatus.Pending)).sorted(Comparator.comparing(CarOrder::getOrderTime)).collect(Collectors.toList());
        Collections.reverse(orders);
        return orders;
    }

    @Override
    public ProductionScheduler copy() {
        return new SpecificationBatchScheduler(carOrderRepository.copy(), timeManager.copy(), new LinkedList<>(currentOrdersOnAssemblyLine.stream().map(c -> c == null ? null : c.copy()).collect(Collectors.toList())));
    }
}
