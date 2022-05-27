package services;

import domain.order.CarOrder;
import domain.scheduler.DateTime;
import domain.scheduler.ProductionScheduler;
import domain.scheduler.TimeManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.CarOrderRepository;
import utils.TestObjects;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductionSchedulerManagerTest {
    private TimeManager timeManager;
    private ProductionScheduler productionScheduler;

    @BeforeEach
    public void setup() {
        timeManager = mock(TimeManager.class);
        productionScheduler = mock(ProductionScheduler.class);
        when(timeManager.getCurrentTime()).thenReturn(new DateTime(3L * 24L * 60L));

    }

    @Test
    void getStatistics() {
        var repo = mock(CarOrderRepository.class);
        var testOrders = TestObjects.delayedTestOrders();
        when(repo.getOrders()).thenReturn(testOrders);

        var PSManager = new ProductionSchedulerManager(productionScheduler, timeManager, repo);

        var s = PSManager.getStatistics();

        assertEquals(s.lastDelay(), 120L);
        assertEquals(s.secondLastDelay(), 60L);

        assertEquals(s.averageDelay(), 70.0);
        assertEquals(s.medianDelay(), 60L);

        assertEquals(s.ordersFinishedYesterday(), 3L);
        assertEquals(s.ordersFinishedDayBefore(), 3L);
    }

    @Test
    void statisticsSameOrderTime() {
        var repo = mock(CarOrderRepository.class);
        var order1 = mock(CarOrder.class);
        var order2 = mock(CarOrder.class);
        when(order1.getOrderTime()).thenReturn(new DateTime(0, 6, 0));
        when(order2.getOrderTime()).thenReturn(new DateTime(0, 6, 0));
        when(order1.getStartTime()).thenReturn(new DateTime(0, 6, 0));
        when(order2.getStartTime()).thenReturn(new DateTime(0, 6, 50));
        when(order1.getEndTime()).thenReturn(new DateTime(0, 9, 20));
        when(order2.getEndTime()).thenReturn(new DateTime(0, 10, 10));
        when(order1.isFinished()).thenReturn(true);
        when(order2.isFinished()).thenReturn(true);
        var testOrders = List.of(order1, order2);

        when(repo.getOrders()).thenReturn(testOrders);

        var manager = new ProductionSchedulerManager(productionScheduler, timeManager, repo);

        var s = manager.getStatistics();

        assertEquals(25, s.averageDelay());

        assertEquals(50, s.lastDelay());
        assertEquals(0, s.secondLastDelay());

        assertEquals(25.0, s.averageDelay());
        assertEquals(25.0, s.medianDelay());

        assertEquals(s.ordersFinishedYesterday(), 0);
        assertEquals(s.ordersFinishedDayBefore(), 0);
    }
}