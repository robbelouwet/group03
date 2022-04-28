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

public class AdaptSchedulingAlgorithmTest {
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
                    case 3 -> "1"; // 5
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
                    case 4 -> assertEquals("Possible Car Options to Give Priority:", l);
                    case 5 -> assertEquals("1. -> {Airco = manual, Wheels = comfort, Color = red, Gearbox = 6 speed manual, Body = sedan, Engine = standard, Seats = leather black}", l); // 4
                    case 6 -> assertEquals("2. -> {Airco = manual, Wheels = comfort, Color = black, Gearbox = 6 speed manual, Body = sedan, Engine = standard, Seats = leather black}", l); // 4
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
