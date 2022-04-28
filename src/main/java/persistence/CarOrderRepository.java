package persistence;

import domain.order.CarOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class that is responsible for keeping track of all car orders in our application
 */
public class CarOrderRepository {
    private final List<CarOrder> orders;
    private final List<CarOrderCatalogObserver> listeners = new ArrayList<>();
    private static CarOrderRepository instance;

    public CarOrderRepository() {
        orders = new ArrayList<>();
    }

    public CarOrderRepository(List<CarOrder> orders) {
        this.orders = orders.stream().map(CarOrder::copy).collect(Collectors.toList());
    }

    public CarOrderRepository copy() {
        return new CarOrderRepository(orders);
    }

    /**
     * Add an order
     * After this call getOrders() will contain this new order
     * Notifies all listeners of the new order
     *
     * @param order should not be null
     */
    public void addOrder(CarOrder order) {
        if (order == null) {
            throw new IllegalArgumentException();
        }
        orders.add(order);
        for (var listener : listeners) {
            if (listener != null) listener.carOrderAdded(order);
        }
    }

    /**
     * @return a list of all orders in this catalog
     */
    public List<CarOrder> getOrders() {
        return orders;
    }

    /**
     * Register a listener to get notified when a new order is added
     *
     * @param listener should not be null
     */
    public void registerListener(CarOrderCatalogObserver listener) {
        if (listener == null) throw new IllegalArgumentException();
        listeners.add(listener);
    }

    /**
     * Remove a listener
     *
     * @param listener should not be null
     */
    public void unregisterListener(CarOrderCatalogObserver listener) {
        if (listener == null) throw new IllegalArgumentException();
        listeners.remove(listener);
    }

    public static CarOrderRepository getInstance() {
        if (instance == null) instance = new CarOrderRepository();
        return instance;
    }


}
