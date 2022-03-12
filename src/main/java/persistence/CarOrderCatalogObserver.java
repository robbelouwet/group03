package persistence;

import domain.order.CarOrder;

public interface CarOrderCatalogObserver {
    void carOrderAdded(CarOrder order);
}
