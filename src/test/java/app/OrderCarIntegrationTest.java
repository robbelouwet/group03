package app;

import app.ui.AppTextView;
import app.ui.interfaces.IAppView;
import app.utils.ConsoleReader;
import app.utils.IConsoleReader;
import org.junit.jupiter.api.Test;
import services.ManagerStore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class OrderCarIntegrationTest {
    @Test
    public void uiTest() {
        ConsoleReader.setInstance(new IConsoleReader() {
            int number = 0;
            int prints = 0;
            @Override
            public String ask(String str) {
                return switch (number++) {
                    case 0 -> "garage holder";
                    case 1, 11 -> "order";
                    case 2, 12 -> "Ford Fiesta";
                    case 3, 13 -> "manual";
                    case 4, 14 -> "comfort";
                    case 5, 15 -> "red";
                    case 6, 16 -> "6 speed manual";
                    case 7, 17 -> "sedan";
                    case 8, 18 -> "standard";
                    case 9, 19 -> "leather black";
                    case 10, 20 -> "confirm";
                    case 21 -> "cancel";
                    default -> "quit";
                };
            }

            @Override
            public void println(String l) {
                switch (prints++) {
                    case 32, 63 -> assertEquals("Order (Ford Fiesta): startTime=Day 0, 6:0, endTime=Day 0, 9:0, status=Pending}", l);
                    case 64 -> assertEquals("Order (Ford Fiesta): startTime=Day 0, 6:0, endTime=Day 0, 10:0, status=Pending}", l);
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
}
