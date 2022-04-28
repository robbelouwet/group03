package app;

import app.ui.AppTextView;
import app.utils.ConsoleReader;
import app.utils.IConsoleReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FullIntegrationTest {
    @Test
    void fullTest() {
        ConsoleReader.setInstance(new IConsoleReader() {
            int asks = 0;
            int prints = 0;
            @Override
            public String ask(String str) {
                return switch (asks++) {
                    case 0 -> "garage holder";
                    case 1 -> "order";
                    case 2 -> "order";
                    case 3 -> "Model A";
                    case 4 -> "Sedan";
                    case 5 -> "Red";
                    case 6 -> "Standard 2l v4";
                    case 7 -> "6 speed manual";
                    case 8 -> "Leather white";
                    case 9 -> "None";
                    case 10 -> "Sports";
                    case 11 -> "None";
                    case 12 -> "confirm";
                    case 13 -> "cancel";
                    case 14 -> "mechanic";
                    case 15 -> "tasks";
                    case 16 -> "Car Body Post";
                    case 17 -> "Assembly car body";
                    case 18 -> "finish";
                    case 19 -> "780";
                    case 20 -> "Paint car";
                    case 21 -> "finish";
                    case 22 -> "30";
                    case 23 -> "cancel";
                    case 24 -> "cancel";
                    case 25 -> "garage holder";
                    case 26 -> "order";
                    case 27 -> "order";
                    case 28 -> "Model C";
                    case 29 -> "Sport";
                    case 30 -> "Black";
                    case 31 -> "Ultra 3l v8";
                    case 32 -> "6 speed manual";
                    case 33 -> "Leather black";
                    case 34 -> "None";
                    case 35 -> "Sports";
                    case 36 -> "High";
                    case 37 -> "confirm";
                    case 38 -> "order";
                    case 39 -> "order";
                    case 40 -> "Model C";
                    case 41 -> "Sport";
                    case 42 -> "Black";
                    case 43 -> "Ultra 3l v8";
                    case 44 -> "6 speed manual";
                    case 45 -> "Leather black";
                    case 46 -> "None";
                    case 47 -> "Sports";
                    case 48 -> "High";
                    case 49 -> "confirm";
                    case 50 -> "cancel";
                    default -> "quit";
                };
            }

            @Override
            public void println(String l) {
                switch (prints++) {
                    case 46 -> assertEquals("Order (Model A): orderTime=Day 0, 06:00, endTime=Day 0, 08:30, status=OnAssemblyLine}", l);
                    case 110, 149 -> assertEquals("Order (Model C): orderTime=Day 0, 19:30, endTime=Day 1, 09:00, status=Pending}", l);
                    case 150 -> assertEquals("Order (Model C): orderTime=Day 0, 19:30, endTime=Day 1, 10:00, status=Pending}", l);
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
