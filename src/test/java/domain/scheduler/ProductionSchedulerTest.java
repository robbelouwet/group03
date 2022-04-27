package domain.scheduler;

import domain.car.CarModel;
import domain.car.CarModelSpecification;
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
    // TODO this entire test needs to be rewritten
    CarOrderRepository repo;
    ProductionScheduler scheduler;
    List<CarOrder> orders = new ArrayList<>();

    @BeforeEach
    void setup() {
        repo = new CarOrderRepository();
        scheduler = new ProductionScheduler(
                repo,
                new TimeManager(),
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
        scheduler.advanced(20, new LinkedList<>(Arrays.asList(null, null, null)));
        assertEquals(new DateTime(0, 8, 50), orders.get(0).getEndTime());
        assertEquals(new DateTime(0, 9, 40), orders.get(1).getEndTime());
        assertEquals(new DateTime(0, 10, 30), orders.get(2).getEndTime());
    }

    @Test
    void recalculatePredictedEndTimesWithOrderOnAssemblyLine() {
        var orderModelB = TestObjects.getCarOrder((new CarCatalog()).getModels().get(1));
        scheduler.advanced(20, new LinkedList<>(Arrays.asList(null, null, orderModelB)));
        assertEquals(new DateTime(0, 9, 50), orderModelB.getEndTime());
        assertEquals(new DateTime(0, 10, 40), orders.get(0).getEndTime());
        assertEquals(new DateTime(0, 11, 30), orders.get(1).getEndTime());
        assertEquals(new DateTime(0, 12, 20), orders.get(2).getEndTime());
    }

    @Test
    void testChangingAlgorithm() {
        Map<OptionCategory, List<Option>> options = new HashMap<>();

        var body = new OptionCategory("Body");
        var color = new OptionCategory("Color");
        var engine = new OptionCategory("Engine");
        var gearbox = new OptionCategory("Gearbox");
        var seats = new OptionCategory("Seats");
        var airco = new OptionCategory("Airco");
        var wheels = new OptionCategory("Wheels");

        options.put(body, List.of(new Option(body, "sedan"), new Option(body, "break")));
        options.put(color, List.of(
                new Option(color, "red"),
                new Option(color, "blue"),
                new Option(color, "black"),
                new Option(color, "white")));
        options.put(engine, List.of(new Option(engine, "standard"), new Option(engine, "performance")));
        options.put(gearbox, List.of(new Option(gearbox, "6 speed manual"), new Option(gearbox, "5 speed automatic")));
        options.put(seats, List.of(
                new Option(seats, "leather black"),
                new Option(seats, "leather white"),
                new Option(seats, "vinyl grey")));
        options.put(airco, List.of(new Option(airco, "manual"), new Option(airco, "automatic")));
        options.put(wheels, List.of(new Option(wheels, "comfort"), new Option(wheels, "sports")));

        CarModelSpecification specification = new CarModelSpecification(options);


        List<CarOrder> testOrders = new ArrayList<>(List.of(
                new CarOrder(
                        new CarModel("Ford Fiesta", specification, 60),
                        Map.of(
                                new OptionCategory("Body"), new Option(body, "sedan"),
                                new OptionCategory("Color"), new Option(color, "red"),
                                new OptionCategory("Engine"), new Option(engine, "standard"),
                                new OptionCategory("Gearbox"), new Option(gearbox, "6 speed manual"),
                                new OptionCategory("Seats"), new Option(seats, "leather black"),
                                new OptionCategory("Airco"), new Option(airco, "manual"),
                                new OptionCategory("Wheels"), new Option(wheels, "comfort")
                        )
                ),
                new CarOrder(
                        new CarModel("Ford Fiesta", specification, 60),
                        Map.of(
                                new OptionCategory("Body"), new Option(body, "sedan"),
                                new OptionCategory("Color"), new Option(color, "red"),
                                new OptionCategory("Engine"), new Option(engine, "standard"),
                                new OptionCategory("Gearbox"), new Option(gearbox, "6 speed manual"),
                                new OptionCategory("Seats"), new Option(seats, "leather black"),
                                new OptionCategory("Airco"), new Option(airco, "manual"),
                                new OptionCategory("Wheels"), new Option(wheels, "comfort")
                        )
                ),
                new CarOrder(
                        new CarModel("Ford Fiesta", specification, 10),
                        Map.of(
                                new OptionCategory("Body"), new Option(body, "sedan"),
                                new OptionCategory("Color"), new Option(color, "red"),
                                new OptionCategory("Engine"), new Option(engine, "standard"),
                                new OptionCategory("Gearbox"), new Option(gearbox, "6 speed manual"),
                                new OptionCategory("Seats"), new Option(seats, "leather black"),
                                new OptionCategory("Airco"), new Option(airco, "manual"),
                                new OptionCategory("Wheels"), new Option(wheels, "comfort")
                        )
                ),
                new CarOrder(
                        new CarModel("Ford Fiesta", specification, 60),
                        Map.of(
                                new OptionCategory("Body"), new Option(body, "sedan"),
                                new OptionCategory("Color"), new Option(color, "black"),
                                new OptionCategory("Engine"), new Option(engine, "standard"),
                                new OptionCategory("Gearbox"), new Option(gearbox, "6 speed manual"),
                                new OptionCategory("Seats"), new Option(seats, "leather black"),
                                new OptionCategory("Airco"), new Option(airco, "manual"),
                                new OptionCategory("Wheels"), new Option(wheels, "comfort")
                        )
                ),
                new CarOrder(
                        new CarModel("Ford Fiesta", specification, 50),
                        Map.of(
                                new OptionCategory("Body"), new Option(body, "sedan"),
                                new OptionCategory("Color"), new Option(color, "black"),
                                new OptionCategory("Engine"), new Option(engine, "standard"),
                                new OptionCategory("Gearbox"), new Option(gearbox, "6 speed manual"),
                                new OptionCategory("Seats"), new Option(seats, "leather black"),
                                new OptionCategory("Airco"), new Option(airco, "manual"),
                                new OptionCategory("Wheels"), new Option(wheels, "comfort")
                        )
                ),
                new CarOrder(
                        new CarModel("Ford Fiesta", specification, 70),
                        Map.of(
                                new OptionCategory("Body"), new Option(body, "sedan"),
                                new OptionCategory("Color"), new Option(color, "black"),
                                new OptionCategory("Engine"), new Option(engine, "standard"),
                                new OptionCategory("Gearbox"), new Option(gearbox, "6 speed manual"),
                                new OptionCategory("Seats"), new Option(seats, "leather black"),
                                new OptionCategory("Airco"), new Option(airco, "manual"),
                                new OptionCategory("Wheels"), new Option(wheels, "comfort")
                        )
                )
        ));
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

        scheduler.advanced(0, new LinkedList<>(Arrays.asList(null, null, null)));
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