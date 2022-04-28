package services;

import domain.scheduler.DateTime;
import domain.scheduler.ProductionScheduler;
import domain.scheduler.TimeManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import persistence.CarOrderRepository;
import persistence.DataSeeder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        var testOrders = DataSeeder.delayedTestOrders();
        when(repo.getOrders()).thenReturn(testOrders);

        var PSManager = new ProductionSchedulerManager(productionScheduler, timeManager, repo);

        var s = PSManager.getStatistics();

        assertEquals(s.lastDelay(), 60L);
        assertEquals(s.secondLastDelay(), 60L);

        assertEquals(s.averageDelay(), 70.0);
        assertEquals(s.medianDelay(), 60L);

        assertEquals(s.ordersFinishedYesterday(), 3L);
        assertEquals(s.ordersFinishedDayBefore(), 3L);
    }
}