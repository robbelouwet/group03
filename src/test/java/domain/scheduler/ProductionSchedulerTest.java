package domain.scheduler;

import domain.car.options.Option;
import domain.car.options.OptionCategory;
import domain.order.CarOrder;
import domain.order.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.CarCatalog;
import persistence.CarOrderRepository;
import utils.TestObjects;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ProductionSchedulerTest {
    CarOrderRepository repo;
    ProductionScheduler scheduler;
    TimeManager timeManager;
    List<CarOrder> orders = new ArrayList<>();

    @BeforeEach
    void setup() {
        timeManager = new TimeManager();
        repo = new CarOrderRepository();
        scheduler = new ProductionScheduler(
                repo,
                timeManager,
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
        assertEquals(new DateTime(0, 11, 0), order.getEndTime());
    }

    @Test
    void recalculatePredictedEndTimes() {
        timeManager.addTime(20);
        scheduler.setCurrentOrdersOnAssemblyLine(new LinkedList<>(Arrays.asList(null, null, null)));
        scheduler.recalculatePredictedEndTimes();
        assertEquals(new DateTime(0, 8, 50), orders.get(0).getEndTime());
        assertEquals(new DateTime(0, 9, 40), orders.get(1).getEndTime());
        assertEquals(new DateTime(0, 10, 30), orders.get(2).getEndTime());
    }

    @Test
    void recalculatePredictedEndTimesWithOrderOnAssemblyLine() {
        var orderModelB = TestObjects.getCarOrder((new CarCatalog()).getModels().get(1));
        timeManager.addTime(20);
        scheduler.setCurrentOrdersOnAssemblyLine(new LinkedList<>(Arrays.asList(null, null, orderModelB)));
        scheduler.recalculatePredictedEndTimes();
        assertEquals(new DateTime(0, 9, 50), orderModelB.getEndTime());
        assertEquals(new DateTime(0, 10, 40), orders.get(0).getEndTime());
        assertEquals(new DateTime(0, 11, 30), orders.get(1).getEndTime());
        assertEquals(new DateTime(0, 12, 20), orders.get(2).getEndTime());
    }

    @Test
    void scheduleWhenNotSameDay() {
        var repo = new CarOrderRepository();
        var timeManager = new TimeManager();
        var scheduler = new ProductionScheduler(repo, timeManager, new FIFOSchedulingAlgorithm());
        var order = TestObjects.getCarOrder();
        order.setStatus(OrderStatus.OnAssemblyLine);
        repo.addOrder(order);
        scheduler.setCurrentOrdersOnAssemblyLine(new LinkedList<>(Arrays.asList(null, order, null)));
        timeManager.addTime(810);
        var order2 = TestObjects.getCarOrder();
        repo.addOrder(order2);
        assertEquals(new DateTime(0, 21, 10), order.getEndTime());
        assertEquals(new DateTime(1, 8, 30), order2.getEndTime());
    }

    @Test
    void testChangingAlgorithm() {
        var body = new OptionCategory("Body");
        var color = new OptionCategory("Color");
        var engine = new OptionCategory("Engine");
        var gearbox = new OptionCategory("Gearbox");
        var seats = new OptionCategory("Seats");
        var airco = new OptionCategory("Airco");
        var wheels = new OptionCategory("Wheels");

        List<CarOrder> testOrders = TestObjects.getCarOrdersForAlgorithm();

        repo = new CarOrderRepository();

        var scheduler = new ProductionScheduler(
                repo,
                new TimeManager(),
                new SpecificationBatchSchedulingAlgorithm(Map.of(
                        new OptionCategory("Body"), new Option(body, "sedan"),
                        new OptionCategory("Color"), new Option(color, "black"),
                        new OptionCategory("Engine"), new Option(engine, "standard"),
                        new OptionCategory("Gearbox"), new Option(gearbox, "6 speed manual"),
                        new OptionCategory("Seats"), new Option(seats, "leather black"),
                        new OptionCategory("Airco"), new Option(airco, "manual"),
                        new OptionCategory("Wheels"), new Option(wheels, "comfort")
                ))
        );
        for (var order : testOrders) repo.addOrder(order);

        scheduler.setCurrentOrdersOnAssemblyLine(new LinkedList<>(Arrays.asList(null, null, null)));
        assertEquals(new DateTime(0, 9, 10), testOrders.get(3).getEndTime());
        assertEquals(new DateTime(0, 10, 20), testOrders.get(4).getEndTime());
        assertEquals(new DateTime(0, 11, 30), testOrders.get(5).getEndTime());
        assertEquals(new DateTime(0, 12, 30), testOrders.get(0).getEndTime());
        assertEquals(new DateTime(0, 13, 30), testOrders.get(1).getEndTime());
        assertEquals(new DateTime(0, 13, 40), testOrders.get(2).getEndTime());

        scheduler.switchAlgorithm(new FIFOSchedulingAlgorithm());

        assertEquals(new DateTime(0, 9, 0), testOrders.get(0).getEndTime());
        assertEquals(new DateTime(0, 10, 0), testOrders.get(1).getEndTime());
        assertEquals(new DateTime(0, 11, 0), testOrders.get(2).getEndTime());
        assertEquals(new DateTime(0, 12, 10), testOrders.get(3).getEndTime());
        assertEquals(new DateTime(0, 13, 20), testOrders.get(4).getEndTime());
        assertEquals(new DateTime(0, 14, 30), testOrders.get(5).getEndTime());
    }
}