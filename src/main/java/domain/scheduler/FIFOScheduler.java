package domain.scheduler;

import domain.order.CarOrder;
import domain.order.OrderStatus;
import persistence.CarOrderRepository;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class FIFOScheduler extends ProductionScheduler {
    FIFOScheduler(CarOrderRepository carOrderRepository, TimeManager timeManager, LinkedList<CarOrder> currentOrdersOnAssemblyLine) {
        super(carOrderRepository, timeManager, currentOrdersOnAssemblyLine);
    }

    @Override
    List<CarOrder> getOrderedListOfPendingOrders() {
        return carOrderRepository.getOrders().stream().filter(o -> o.getStatus().equals(OrderStatus.Pending)).sorted(Comparator.comparing(CarOrder::getOrderTime)).collect(Collectors.toList());
    }

    @Override
    public ProductionScheduler copy() {
        return new FIFOScheduler(carOrderRepository.copy(), timeManager.copy(), new LinkedList<>(currentOrdersOnAssemblyLine.stream().map(c -> c == null ? null : c.copy()).collect(Collectors.toList())));
    }
}
