package app;

import app.ui.AppTextView;
import app.ui.interfaces.IAppView;
import app.utils.ConsoleReader;
import app.utils.IConsoleReader;
import org.junit.jupiter.api.Test;
import services.ManagerStore;

import java.util.ArrayList;
import java.util.List;

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
                    case 1, 13, 24 -> "order";  // 2
                    case 2 -> "Model A";  // 4
                    case 14 -> "Model B"; // 4
                    case 25 -> "Model C"; // 4
                    case 3 -> "bla";  // wrong input
                    case 4, 15 -> "Sedan"; // 6
                    case 26 -> "Sport"; // 6
                    case 5, 16, 27 -> "Red";  // 6
                    case 6, 17, 28 -> "Standard 2l v4";  // 6
                    case 7, 18, 29 -> "6 speed manual";  // 6
                    case 8, 19, 30 -> "Leather black";  // 6
                    case 9, 20, 31 -> "Automatic";  // 6
                    case 10, 21, 32 -> "Comfort";  // 6
                    case 11, 22, 33 -> "None";  // 6
                    case 12, 23 -> "confirm";  // 6
                    case 34 -> "cancel"; // 6.a
                    case 35 -> "cancel";  // 1.a
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
                    case 44 -> assertEquals("Predicted end time: Day 0, 8:30", l);  // 8
                    case 91 -> assertEquals("Predicted end time: Day 0, 10:20", l);  // 8
                    case 46 -> assertEquals("Order (Model A): orderTime=Day 0, 6:0, endTime=Day 0, 8:30, status=Pending}", l);  // 1
                    case 93, 139 -> assertEquals("Order (Model A): orderTime=Day 0, 6:0, endTime=Day 0, 9:10, status=Pending}", l);  // 1
                    case 94, 140 -> assertEquals("Order (Model B): orderTime=Day 0, 6:0, endTime=Day 0, 10:20, status=Pending}", l);  // 1
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
