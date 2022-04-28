package app;

import app.ui.AppTextView;
import app.utils.ConsoleReader;
import app.utils.IConsoleReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderNewCarIntegrationTest {
    @Test
    public void uiTest() {
        ConsoleReader.setInstance(new IConsoleReader() {
            int number = 0;
            int prints = 0;
            @Override
            public String ask(String str) {
                return switch (number++) {
                    case 0 -> "garage holder";
                    case 1 -> "order";  // The garage holder has to choose between order or details
                    case 2, 14, 25 -> "order";  // 2
                    case 3 -> "Model A";  // 4
                    case 15 -> "Model B"; // 4
                    case 26 -> "Model C"; // 4
                    case 4 -> "bla";  // wrong input
                    case 5, 16 -> "Sedan"; // 6
                    case 27 -> "Sport"; // 6
                    case 6, 17, 28 -> "Red";  // 6
                    case 7, 18, 29 -> "Standard 2l v4";  // 6
                    case 8, 19, 30 -> "6 speed manual";  // 6
                    case 9, 20, 31 -> "Leather black";  // 6
                    case 10, 21, 32 -> "Automatic";  // 6
                    case 11, 22, 33 -> "Comfort";  // 6
                    case 12, 23, 34 -> "None";  // 6
                    case 13, 24 -> "confirm";  // 6
                    case 35 -> "cancel"; // 6.a
                    case 36 -> "cancel";  // 1.a
                    default -> "quit";
                };
            }

            @Override
            public void println(String l) {
                switch (prints++) {
                    case 1 -> assertEquals("Pending orders:", l);  // 1
                    case 2, 47, 141 -> assertEquals("Finished orders:", l); // 1
                    case 3 -> assertEquals("Carmodel options:", l);  // 3
                    case 4 -> assertEquals("Model A", l);  // 3
                    case 5 -> assertEquals("Model B", l);  // 3
                    case 6 -> assertEquals("Model C", l);  // 3
                    case 7 -> assertEquals("Select an option or cancel [cancel]", l);  // 5
                    case 8 -> assertEquals("Category: Body", l);  // 5
                    case 9 -> assertEquals("Sedan", l);  // 5
                    case 10 -> assertEquals("Break", l);  // 5
                    case 12 -> assertEquals("Category: Color", l);  // 5
                    case 13 -> assertEquals("Red", l);  // 5
                    case 14 -> assertEquals("Blue", l);  // 5
                    case 44 -> assertEquals("Predicted end time: Day 0, 08:30", l);  // 8
                    case 91 -> assertEquals("Predicted end time: Day 0, 10:20", l);  // 8
                    case 46 -> assertEquals("Order (Model A): orderTime=Day 0, 06:00, endTime=Day 0, 08:30, status=Pending}", l);  // 1
                    case 93, 139 -> assertEquals("Order (Model A): orderTime=Day 0, 06:00, endTime=Day 0, 09:10, status=Pending}", l);  // 1
                    case 94, 140 -> assertEquals("Order (Model B): orderTime=Day 0, 06:00, endTime=Day 0, 10:20, status=Pending}", l);  // 1
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
