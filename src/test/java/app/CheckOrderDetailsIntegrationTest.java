package app;

import app.controllers.ControllerStore;
import app.ui.AppTextView;
import app.utils.ConsoleReader;
import app.utils.IConsoleReader;
import domain.order.CarOrder;
import domain.order.OrderStatus;
import domain.scheduler.DateTime;
import org.junit.jupiter.api.Test;
import persistence.CarCatalog;
import persistence.CarOrderRepository;
import services.ManagerStore;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static utils.TestObjects.getCarOrder;

public class CheckOrderDetailsIntegrationTest {
    @Test
    public void uiTest() {
        ConsoleReader.setInstance(new IConsoleReader() {
            int number = 0;
            int prints = 0;

            @Override
            public String ask(String str) {
                return switch (number++) {
                    case 0 -> "garage holder";
                    case 1 -> "details";
                    case 2 -> "0";  // 2
                    case 3 -> "other"; // 4.a
                    case 4 -> "6";  // This should ask again, since it goes from 0 to 5
                    case 5 -> "5";  // 2
                    case 6 -> "cancel";  // 4
                    default -> "quit";
                };
            }

            @Override
            public void println(String l) {
                switch (prints++) {
                    case 0 -> assertEquals("Pending Orders:", l);  // 1
                    case 1 ->
                            assertEquals("0: Order (Model C): orderTime=Day 1, 08:00, endTime=Day 0, 11:20, status=Pending}", l);  // 1
                    case 2 ->
                            assertEquals("1: Order (Model B): orderTime=Day 1, 07:00, endTime=Day 0, 10:20, status=Pending}", l);  // 1
                    case 3 ->
                            assertEquals("2: Order (Model A): orderTime=Day 1, 06:00, endTime=Day 0, 09:10, status=Pending}", l);  // 1
                    case 4 -> assertEquals("Finished Orders:", l);  // 1
                    case 5 ->
                            assertEquals("3: Order (Model C): orderTime=Day 0, 08:00, endTime=Day 0, 10:10, status=Finished}", l);  // 1
                    case 6 ->
                            assertEquals("4: Order (Model B): orderTime=Day 0, 07:00, endTime=Day 0, 10:10, status=Finished}", l);  // 1
                    case 7 ->
                            assertEquals("5: Order (Model A): orderTime=Day 0, 06:00, endTime=Day 0, 10:10, status=Finished}", l);  // 1
                    case 8 -> assertEquals("""
                            Order (Model C): orderTime=Day 1, 08:00, endTime=Day 0, 11:20, status=Pending}
                            Body: Sport
                            Color: Black
                            Engine: Performance 2.5l v6
                            Gearbox: 6 speed manual
                            Seats: Leather white
                            Airco: Manual
                            Wheels: Winter
                            Spoiler: High
                            """, l); // 3
                    case 17 -> assertEquals("""
                            Order (Model A): orderTime=Day 0, 06:00, endTime=Day 0, 10:10, status=Finished}
                            Body: Sedan
                            Color: Red
                            Engine: Standard 2l v4
                            Gearbox: 6 speed manual
                            Seats: Leather white
                            Airco: Manual
                            Wheels: Winter
                            Spoiler: None
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
        // Create a testing repo with one order for each model
        var models = (new CarCatalog()).getModels();
        List<CarOrder> orders = IntStream.range(0, models.size()).mapToObj(i -> getCarOrder(models.get(i), new DateTime(1, 6 + i, 0))).collect(Collectors.toList());
        List<CarOrder> orders2 = IntStream.range(0, models.size()).mapToObj(i -> getCarOrder(models.get(i), new DateTime(0, 6 + i, 0))).toList();
        for (var order : orders2) {
            order.setStatus(OrderStatus.Finished);
            order.setEndTime(new DateTime(0, 10, 10));
        }
        orders.addAll(orders2);

        var carOrderRepo = new CarOrderRepository(orders);
        var managerStore = new ManagerStore(carOrderRepo);
        var view = new AppTextView(new ControllerStore(managerStore));
        view.start();
    }

    @Test
    public void testWithoutOrdersReturnsWithoutAsking() {
        ConsoleReader.setInstance(new IConsoleReader() {
            int number = 0;
            int prints = 0;
            @Override
            public String ask(String str) {
                return switch (number++) {
                    case 0 -> "garage holder";
                    case 1 -> "details";
                    case 2 -> "0";  // Not valid so should ask again
                    case 3 -> "cancel";  // Cancel when application asks again
                    default -> "quit";
                };
            }

            @Override
            public void println(String l) {
                switch (prints++) {
                    case 0 -> assertEquals("Pending Orders:", l);
                    case 1 -> assertEquals("Finished Orders:", l);
                }
            }

            @Override
            public void print(String s) {

            }

            @Override
            public void printf(String format, Object obj) {

            }
        });

        var view = new AppTextView(new ControllerStore());
        view.start();
    }
}
