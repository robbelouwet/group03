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
        ManagerStore.getInstance().init();

        ConsoleReader.setInstance(new IConsoleReader() {
            int number = 0;
            int prints = 0;
            @Override
            public String ask(String str) {
                return switch (number++) {
                    case 0 -> "garage holder";
                    case 1 -> "order";
                    case 2 -> "Ford Fiesta";
                    case 3 -> "manual";
                    case 4 -> "comfort";
                    case 5 -> "red";
                    case 6 -> "6 speed manual";
                    case 7 -> "sedan";
                    case 8 -> "standard";
                    case 9 -> "leather black";
                    case 10 -> "confirm";
                    case 11 -> "order";
                    case 12 -> "Ford Fiesta";
                    case 13 -> "manual";
                    case 14 -> "comfort";
                    case 15 -> "red";
                    case 16 -> "6 speed manual";
                    case 17 -> "sedan";
                    case 18 -> "standard";
                    case 19 -> "leather black";
                    case 20 -> "confirm";
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
