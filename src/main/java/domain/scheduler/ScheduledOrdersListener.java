package domain.scheduler;

import domain.order.CarOrder;

public interface ScheduledOrdersListener {
    void orderScheduled(CarOrder order);
}
