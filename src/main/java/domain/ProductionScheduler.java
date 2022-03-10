package domain;

import domain.car.CarOrder;
import lombok.Getter;
import persistence.CarOrderCatalog;
import persistence.CarOrderCatalogObserver;

import java.time.Duration;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ProductionScheduler implements CarOrderCatalogObserver {
    @Getter
    private static final ProductionScheduler instance = new ProductionScheduler();

    private final CarOrderCatalog carOrderCatalog;

    private ProductionScheduler() {
        carOrderCatalog = CarOrderCatalog.getInstance();
        carOrderCatalog.registerListener(this);
    }

    public CarOrder getNextProcess(){
        var orders = carOrderCatalog.getOrders().stream().filter(o -> o.getStatus().equals(OrderStatus.Pending)).sorted(Comparator.comparing(CarOrder::getStartTime)).collect(Collectors.toList());
        if (orders.size() == 0) {
            return null;
        }
        CarOrder order = orders.get(0);
        order.setStatus(OrderStatus.OnAssemblyLine);
        return order;
    }

    private void delayed(Duration delay) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void carOrderAdded(CarOrder order) {
        // TODO calculate
        order.setEndTime(order.getStartTime().plusDays(1));
    }
}
