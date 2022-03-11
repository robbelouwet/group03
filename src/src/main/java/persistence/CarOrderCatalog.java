package persistence;

import domain.car.CarOrder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CarOrderCatalog {
    @Getter
    private static final  CarOrderCatalog instance = new CarOrderCatalog();
    private final List<CarOrder> orders = new ArrayList<>();
    private final List<CarOrderCatalogObserver> listeners = new ArrayList<>();

    public void addOrder(CarOrder order) {
        orders.add(order);
        System.out.println(listeners);
        for (var listener : listeners) listener.carOrderAdded(order);
    }

    public List<CarOrder> getOrders() {
        return new ArrayList<>(orders);
    }

    public void registerListener(CarOrderCatalogObserver listener) {
        listeners.add(listener);
    }
}
