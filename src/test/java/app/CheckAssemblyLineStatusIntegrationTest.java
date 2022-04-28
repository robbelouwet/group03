package app;

import app.controllers.ControllerStore;
import app.ui.AppTextView;
import app.utils.ConsoleReader;
import app.utils.IConsoleReader;
import domain.assembly.AssemblyLine;
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

public class CheckAssemblyLineStatusIntegrationTest {
    private ManagerStore managerStore;
    private ControllerStore controllerStore;
    private CarOrderRepository repo;
    private AssemblyLine assemblyLine;
    private ProductionScheduler scheduler;
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
                    case 0 -> "mechanic";
                    case 1 -> "status";
                    default -> "quit";
                };
            }

            @Override
            public void println(String l) {
                switch (prints++) {
                    case 0 -> assertEquals("Hi mechanic!", l);
                }
            }

            @Override
            public void printf(String format, Object obj) {
                switch (prints++) {
                    case 1 -> assertEquals("Test", obj);
                    case 2, 3 -> assertEquals("Nothing", obj);
                }
            }

            @Override
            public void print(String l) {

            }
        });

        orders.addAll(TestObjects());

        for (var order : orders) {
            repo.addOrder(order);
        }

        var carOrderRepo = new CarOrderRepository(orders);
        var managerStore = new ManagerStore(carOrderRepo);
        var view = new AppTextView(new ControllerStore(managerStore));
        view.start();
    }

    @Test
    public void testStatusWithoutOrders() {
        ConsoleReader.setInstance(new IConsoleReader() {
            int number = 0;
            int prints = 0;

            @Override
            public String ask(String str) {
                return switch (number++) {
                    case 0 -> "mechanic";
                    case 1 -> "status";
                    default -> "quit";
                };
            }

            @Override
            public void println(String l) {
                switch (prints++) {
                    case 0 -> assertEquals("Hi mechanic!", l);
                }
            }

            @Override
            public void print(String s) {

            }

            @Override
            public void printf(String format, Object obj) {
                switch (prints++) {
                    case 1 -> assertEquals("Empty", obj);
                    case 2, 3 -> assertEquals("Nothing", obj);
                }
            }
        });

        var view = new AppTextView(new ControllerStore());
        view.start();
    }
}
