package app;

import app.controllers.ControllerStore;
import app.ui.AppTextView;
import app.utils.ConsoleReader;
import app.utils.IConsoleReader;
import domain.order.CarOrder;
import domain.order.OrderStatus;
import domain.scheduler.DateTime;
import org.junit.jupiter.api.Test;
import persistence.CarOrderRepository;
import services.ManagerStore;
import utils.TestObjects;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatisticsIntegrationTest {
    @Test
    public void noOrdersFinishedTest() {
        ConsoleReader.setInstance(new IConsoleReader() {
            int number = 0;
            int prints = 0;

            @Override
            public String ask(String str) {
                return switch (number++) {
                    case 0 -> "manager";
                    case 1 -> "statistics";
                    default -> "quit";
                };
            }

            @Override
            public void println(String l) {
                switch (prints++) {
                    case 0 -> assertEquals("Hi manager!", l);
                    case 1 -> assertEquals("""
                            STATISTICS
                                                        
                            No orders were finished!
                            """, l);
                }
            }

            @Override
            public void print(String s) {
            }

            @Override
            public void printf(String format, Object obj) {

            }
        });

        var view = new AppTextView();
        view.start();
    }

    @Test
    public void statisticsCheck() {
        List<CarOrder> testOrders = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            var order = TestObjects.getCarOrder();
            order.setOrderTime(new DateTime(0, 6, 0));
            order.setStartTime(new DateTime(6 * 60 + i * i * 60));
            order.setStatus(OrderStatus.Finished);
            order.setEndTime(new DateTime(9 * 60 + i * i * 60));
            testOrders.add(order);
        }
        var repo = new CarOrderRepository(testOrders);

        ConsoleReader.setInstance(new IConsoleReader() {
            int number = 0;
            int prints = 0;

            @Override
            public String ask(String str) {
                return switch (number++) {
                    case 0 -> "manager";
                    case 1 -> "statistics";
                    default -> "quit";
                };
            }

            @Override
            public void println(String l) {
                switch (prints++) {
                    case 0 -> assertEquals("Hi manager!", l);
                    case 1 -> assertEquals("""
                            STATISTICS
                                                        
                            Recent delays
                            - Last delay was 4860 minutes, at Day 3, 15:00
                            - Second last delay was 3840 minutes, at Day 2, 22:00 minutes
                            Average and median
                            - The median amount of delay was: 1230.0 minutes, the average was 1710.0 minutes
                            - The median amount of finished orders per day is: 2.0, the average is 2.5
                            Recent finished
                            - Yesterday, 0 orders got finished
                            - The day before that: 0
                            """, l);
                }
            }

            @Override
            public void print(String s) {
            }

            @Override
            public void printf(String format, Object obj) {

            }
        });

        var managerStore = new ManagerStore(repo);
        var controllerStore = new ControllerStore(managerStore);
        var view = new AppTextView(controllerStore);
        view.start();
    }

    @Test
    public void statisticsCheck2() {
        var repo = new CarOrderRepository(TestObjects.delayedTestOrders());

        ConsoleReader.setInstance(new IConsoleReader() {
            int number = 0;
            int prints = 0;

            @Override
            public String ask(String str) {
                return switch (number++) {
                    case 0 -> "manager";
                    case 1 -> "statistics";
                    default -> "quit";
                };
            }

            @Override
            public void println(String l) {
                switch (prints++) {
                    case 0 -> assertEquals("Hi manager!", l);
                    case 1 -> assertEquals("""
                            STATISTICS
                                                        
                            Recent delays
                            - Last delay was 120 minutes, at Day 2, 02:00
                            - Second last delay was 60 minutes, at Day 2, 01:00 minutes
                            Average and median
                            - The median amount of delay was: 60.0 minutes, the average was 70.0 minutes
                            - The median amount of finished orders per day is: 3.0, the average is 3.0
                            Recent finished
                            - Yesterday, 0 orders got finished
                            - The day before that: 0
                            """, l);
                }
            }

            @Override
            public void print(String s) {
            }

            @Override
            public void printf(String format, Object obj) {

            }
        });

        var managerStore = new ManagerStore(repo);
        var controllerStore = new ControllerStore(managerStore);
        var view = new AppTextView(controllerStore);
        view.start();
    }
}
