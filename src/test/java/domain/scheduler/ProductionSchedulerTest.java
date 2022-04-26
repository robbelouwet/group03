package domain.scheduler;

import domain.order.CarOrder;
import domain.order.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.CarOrderRepository;
import utils.TestObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ProductionSchedulerTest {
    // TODO this entire test needs to be rewritten
    CarOrderRepository repo;
    ProductionScheduler scheduler;
    List<CarOrder> orders = new ArrayList<>();

    @BeforeEach
    void setup() {
        repo =  new CarOrderRepository();
        scheduler = new ProductionScheduler(
                repo,
                new TimeManager(),
                new LinkedList<>(),
                new FIFOSchedulingAlgorithm()
        );

        for (int i = 0; i < 3; i++) {
            orders.add(TestObjects.getCarOrder());
        }
        for (var order : orders) {
            repo.addOrder(order);
        }
    }

    @Test
    void getNextOrder() {
        assertSame(orders.get(0), scheduler.getNextOrder());
        orders.get(0).setStatus(OrderStatus.Finished);
        assertSame(orders.get(1), scheduler.getNextOrder());
    }

    @Test
    void carOrderAdded() {
        var order = TestObjects.getCarOrder();
        repo.addOrder(order);
        assertEquals(new DateTime(0, 12, 30), order.getEndTime());
    }

    @Test
    void recalculatePredictedEndTimes() {
        scheduler.advanced(20, new LinkedList<>(Arrays.asList(null, null, null)));
        assertEquals(new DateTime(0, 10, 0), orders.get(0).getEndTime());
        assertEquals(new DateTime(0, 11, 0), orders.get(1).getEndTime());
        assertEquals(new DateTime(0, 12, 0), orders.get(2).getEndTime());
    }
}