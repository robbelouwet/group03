package domain.scheduler;

import domain.OrderStatus;
import domain.car.CarOrder;
import persistence.CarOrderCatalog;
import persistence.CarOrderCatalogObserver;

import java.time.Duration;
import java.util.Comparator;
import java.util.stream.Collectors;

// package-private!
class FCFSProductionScheduler extends ProductionScheduler implements CarOrderCatalogObserver {
    private final CarOrderCatalog carOrderCatalog;

    FCFSProductionScheduler() {
        carOrderCatalog = CarOrderCatalog.getInstance();
        carOrderCatalog.registerListener(this);
    }

    @Override
    public void addOrder(CarOrder order) {
        throw new UnsupportedOperationException();

    }

    @Override
    public CarOrder getNextOrder(){
        var orders = carOrderCatalog.getOrders().stream().filter(o -> o.getStatus().equals(OrderStatus.Pending)).sorted(Comparator.comparing(CarOrder::getStartTime)).collect(Collectors.toList());
        if (orders.size() == 0) {
            return null;
        }
        CarOrder order = orders.get(0);
        order.setStatus(OrderStatus.OnAssemblyLine);
        return order;
    }

    @Override
    public void updateSchedule(CarOrder process) {
        throw new UnsupportedOperationException();
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
