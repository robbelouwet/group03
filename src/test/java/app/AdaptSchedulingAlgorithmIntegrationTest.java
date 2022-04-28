package app;

import app.controllers.ControllerStore;
import app.ui.AppTextView;
import app.utils.ConsoleReader;
import app.utils.IConsoleReader;
import domain.order.CarOrder;
import domain.scheduler.FIFOSchedulingAlgorithm;
import domain.scheduler.ProductionScheduler;
import domain.scheduler.TimeManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.CarOrderRepository;
import services.ManagerStore;
import utils.TestObjects;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AdaptSchedulingAlgorithmIntegrationTest {
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
    }

    @Test
    public void uiTest() {
        ConsoleReader.setInstance(new IConsoleReader() {
            int number = 0;
            int prints = 0;

            @Override
            public String ask(String str) {
                return switch (number++) {
                    case 0, 4 -> "manager";
                    case 1, 5 -> "algorithm"; // 1
                    case 2 -> "SB"; // 3.a
                    case 3 -> "1"; // 5 (Specification Batch)
                    case 6 -> "cancel"; // 2.a
                    default -> "quit";
                };
            }

            @Override
            public void println(String l) {
                switch (prints++) {
                    case 0, 7 -> assertEquals("Hi manager!", l);
                    case 1, 8 -> assertEquals("Possible Scheduling Algorithms:", l); // 1
                    case 2 -> assertEquals("FIFO: (X)", l); // 1
                    case 3 -> assertEquals("SB", l); // 1
                    case 4 -> assertEquals("Possible Car Options to Give Priority:", l); // 4 (Specification Batch)
                    case 5 -> {
                        assertTrue(l.contains("Seats = Leather black"));
                        assertTrue(l.contains("Body = Sedan"));
                        assertTrue(l.contains("Color = Red"));
                        assertTrue(l.contains("Engine = Standard 2l v4"));
                        assertTrue(l.contains("Spoiler = None"));
                        assertTrue(l.contains("Airco = Manual"));
                        assertTrue(l.contains("Wheels = Comfort"));
                        assertTrue(l.contains("Gearbox = 6 speed manual"));
                    } // 4
                    case 6 -> {
                        assertTrue(l.contains("Seats = Leather black"));
                        assertTrue(l.contains("Body = Sedan"));
                        assertTrue(l.contains("Color = Black"));
                        assertTrue(l.contains("Engine = Standard 2l v4"));
                        assertTrue(l.contains("Spoiler = None"));
                        assertTrue(l.contains("Airco = Manual"));
                        assertTrue(l.contains("Wheels = Comfort"));
                        assertTrue(l.contains("Gearbox = 6 speed manual"));
                    }
                    // 4
                    case 9 -> assertEquals("FIFO", l); // 4
                    case 10 -> assertEquals("SB: (X)", l); // 4
                }
            }

            @Override
            public void print(String s) {
            }

            @Override
            public void printf(String format, Object obj) {

            }
        });

        orders.addAll(TestObjects.getCarOrdersForAlgorithm());

        for (var order : orders) {
            repo.addOrder(order);
        }

        var carOrderRepo = new CarOrderRepository(orders);
        var managerStore = new ManagerStore(carOrderRepo);
        var view = new AppTextView(new ControllerStore(managerStore));
        view.start();
    }
}
