package persistence;

import domain.order.CarOrder;

public interface CarOrderRepositoryObserver {
    void carOrderAdded(CarOrder order);
}
