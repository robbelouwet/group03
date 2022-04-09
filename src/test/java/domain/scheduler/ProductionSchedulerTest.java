package domain.scheduler;

import domain.order.CarOrder;
import domain.order.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.CarCatalog;
import persistence.CarOrderRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ProductionSchedulerTest {
    // TODO this entire test needs to be rewritten
    CarOrderRepository repo;
    ProductionScheduler scheduler;
    List<CarOrder> orders = new ArrayList<>();

    @BeforeEach
    void setup() {
        repo =  new CarOrderRepository();
        scheduler = ProductionScheduler.of(repo, "FIFO");

        for (int i = 0; i < 3; i++) {
            orders.add(new CarOrder(new DateTime(2), CarCatalog.getModels().get(0), new HashMap<>() {{
                put("Body", "break");
                put("Color", "white");
                put("Engine", "performance");
                put("Gearbox", "5 speed automatic");
                put("Seats", "vinyl grey");
                put("Airco", "automatic");
                put("Wheels", "sports");
            }}));
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
        var order = new CarOrder(new DateTime(2), CarCatalog.getModels().get(0), new HashMap<>() {{
            put("Body", "break");
            put("Color", "white");
            put("Engine", "performance");
            put("Gearbox", "5 speed automatic");
            put("Seats", "vinyl grey");
            put("Airco", "automatic");
            put("Wheels", "sports");
        }});
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