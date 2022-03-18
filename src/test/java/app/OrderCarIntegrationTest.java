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
                    case 1, 12, 22 -> "order";  // 2
                    case 2, 13, 23 -> "Ford Fiesta";  // 4
                    case 3 -> "bla";  // wrong input
                    case 4 -> "manual"; // Will ask again on wrong input
                    case 14, 24 -> "manual";  // 6
                    case 5, 15, 25 -> "comfort";  // 6
                    case 6, 16, 26 -> "red";  // 6
                    case 7, 17, 27 -> "6 speed manual";  // 6
                    case 8, 18, 28 -> "sedan";  // 6
                    case 9, 19, 29 -> "standard";  // 6
                    case 10, 20, 30 -> "leather black";  // 6
                    case 11, 21 -> "confirm";  // 6
                    case 31 -> "cancel"; // 6.a
                    case 32 -> "cancel";  // 1.a
                    default -> "quit";
                };
            }

            @Override
            public void println(String l) {
                switch (prints++) {
                    case 1 -> assertEquals("Pending orders:", l);  // 1
                    case 2 -> assertEquals("Finished orders:", l); // 1
                    case 3 -> assertEquals("Carmodel options:", l);  // 3
                    case 4 -> assertEquals("Ford Fiesta", l);  // 3
                    case 5 -> assertEquals("Make a selection for each option, or type [cancel]", l);  // 5
                    case 6 -> assertEquals("Option: Airco", l);  // 5
                    case 7 -> assertEquals("manual", l);  // 5
                    case 8 -> assertEquals("automatic", l);  // 5
                    case 32, 63, 94 -> assertEquals("Order (Ford Fiesta): startTime=Day 0, 6:0, endTime=Day 0, 9:0, status=Pending}", l);
                    case 64, 95 -> assertEquals("Order (Ford Fiesta): startTime=Day 0, 6:0, endTime=Day 0, 10:0, status=Pending}", l);
                    case 96 -> assertEquals("Finished orders:", l);
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
