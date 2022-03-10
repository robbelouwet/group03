package persistence;

import domain.car.CarOrder;

public interface CarOrderCatalogObserver {
    void carOrderAdded(CarOrder order);
}
