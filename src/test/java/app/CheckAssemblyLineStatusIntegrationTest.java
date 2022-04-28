package app;

import app.controllers.ControllerStore;
import app.ui.AppTextView;
import app.utils.ConsoleReader;
import app.utils.IConsoleReader;
import domain.car.options.Option;
import domain.car.options.OptionCategory;
import domain.car.options.OptionSelector;
import org.junit.jupiter.api.Test;
import services.ManagerStore;
import utils.TestObjects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckAssemblyLineStatusIntegrationTest {

    @Test
    public void uiTest() {
        ConsoleReader.setInstance(new IConsoleReader() {
            int number = 0;
            int prints = 0;

            @Override
            public String ask(String str) {
                return switch (number++) {
                    case 0 -> "mechanic"; // 1
                    case 1 -> "status"; // 1
                    default -> "quit";
                };
            }

            @Override
            public void println(String l) {
                if (prints++ == 0) {
                    assertEquals("Hi mechanic!", l);
                }
            }

            @Override
            public void printf(String format, Object obj) {
                switch (prints++) {
                    case 1, 4 -> assertEquals("", obj);
                    case 2, 5 -> assertEquals("Car Body Post", obj); // 2
                    case 3 -> assertEquals("Order (Model A): orderTime=Day 0, 06:00, endTime=Day 0, 08:30, status=OnAssemblyLine}", obj); // 2
                    case 6 -> assertEquals("Task [Assembly car body]: Sedan (pending)", obj); // 2
                    case 7 -> assertEquals("Task [Paint car]: Red (pending)", obj); // 2
                    case 8 -> assertEquals("Nothing", obj); // 2
                }
            }

            @Override
            public void print(String l) {

            }
        });

        var selector = new OptionSelector(TestObjects.getCarOptions());
        var model = TestObjects.getCarOrder().getModel();

        selector.selectOption(new Option(new OptionCategory("Body"), "Sedan"));
        selector.selectOption(new Option(new OptionCategory("Color"), "Red"));
        selector.selectOption(new Option(new OptionCategory("Engine"), "Standard 2l v4"));
        selector.selectOption(new Option(new OptionCategory("Gearbox"), "6 speed manual"));
        selector.selectOption(new Option(new OptionCategory("Seats"), "Leather white"));
        selector.selectOption(new Option(new OptionCategory("Airco"), "Manual"));
        selector.selectOption(new Option(new OptionCategory("Wheels"), "Winter"));
        selector.selectOption(new Option(new OptionCategory("Spoiler"), "None"));

        var managerStore = new ManagerStore();
        var controllerStore = new ControllerStore(managerStore);
        managerStore.getCarOrderManager().submitCarOrder(model, selector);
        var view = new AppTextView(controllerStore);
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
                    case 0 -> "mechanic"; // 1
                    case 1 -> "status"; // 1
                    default -> "quit";
                };
            }

            @Override
            public void println(String l) {
                if (prints++ == 0) {
                    assertEquals("Hi mechanic!", l);
                }
            }

            @Override
            public void print(String s) {

            }

            @Override
            public void printf(String format, Object obj) {
                switch (prints++) {
                    case 1 -> assertEquals("Empty", obj); // 2
                    case 2, 3 -> assertEquals("Nothing", obj); // 2
                }
            }
        });

        var view = new AppTextView(new ControllerStore());
        view.start();
    }
}
