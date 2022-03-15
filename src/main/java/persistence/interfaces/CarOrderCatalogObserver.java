package persistence.interfaces;

import domain.order.CarOrder;

public interface CarOrderCatalogObserver {
    void carOrderAdded(CarOrder order);
}
