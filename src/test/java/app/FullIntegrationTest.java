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
                    case 51 -> "mechanic";
                    case 52 -> "status";
                    case 53 -> "garage holder";
                    case 54 -> "order";
                    case 55 -> "order";
                    case 56 -> "Model C";
                    case 57 -> "Sport";
                    case 58 -> "Black";
                    case 59 -> "Ultra 3l v8";
                    case 60 -> "6 speed manual";
                    case 61 -> "Leather black";
                    case 62 -> "None";
                    case 63 -> "Sports";
                    case 64 -> "High";
                    case 65 -> "confirm";
                    case 66 -> "order";
                    case 67 -> "order";
                    case 68 -> "Model C";
                    case 69 -> "Sport";
                    case 70 -> "Black";
                    case 71 -> "Ultra 3l v8";
                    case 72 -> "6 speed manual";
                    case 73 -> "Leather black";
                    case 74 -> "None";
                    case 75 -> "Sports";
                    case 76 -> "High";
                    case 77 -> "confirm";
                    case 78 -> "order";
                    case 79 -> "garage holder";
                    case 80 -> "order";
                    case 81 -> "order";
                    case 82 -> "Model C";
                    case 83 -> "Sport";
                    case 84 -> "Black";
                    case 85 -> "Ultra 3l v8";
                    case 86 -> "6 speed manual";
                    case 87 -> "Leather black";
                    case 88 -> "None";
                    case 89 -> "Sports";
                    case 90 -> "High";
                    case 91 -> "confirm";
                    case 92 -> "order";
                    case 93, 106 -> "order";
                    case 94, 107 -> "Model B";
                    case 95, 108 -> "Sport";
                    case 96, 109 -> "Green";
                    case 97, 110 -> "Ultra 3l v8";
                    case 98, 111 -> "6 speed manual";
                    case 99, 112 -> "Leather black";
                    case 100, 113 -> "None";
                    case 101, 114 -> "Sports";
                    case 102, 115 -> "Low";
                    case 103, 116 -> "confirm";
                    case 105 -> "order";
                    case 128 -> "cancel";
                    case 129 -> "mechanic";
                    case 130 -> "tasks";
                    case 131 -> "Drivetrain Post";
                    case 132 -> "Insert engine";
                    case 133 -> "finish";
                    case 134 -> "10";
                    case 135 -> "Insert gearbox";
                    case 136 -> "finish";
                    case 137 -> "40";
                    case 138 -> "cancel";
                    case 139 -> "Accessories Post";
                    case 140 -> "Install seats";
                    case 141 -> "finish";
                    case 142 -> "10";
                    case 143 -> "Install airco";
                    case 144 -> "finish";
                    case 145 -> "10";
                    case 146 -> "Mount wheels";
                    case 147 -> "finish";
                    case 148 -> "30";
                    case 149 -> "cancel";
                    case 150 -> "cancel";
                    case 151 -> "garage holder";
                    case 152 -> "order";
                    case 153 -> "order";
                    case 154 -> "Model C";
                    case 155 -> "Sport";
                    case 156 -> "Black";
                    case 157 -> "Ultra 3l v8";
                    case 158 -> "6 speed manual";
                    case 159 -> "Leather black";
                    case 160 -> "None";
                    case 161 -> "Sports";
                    case 162 -> "High";
                    case 163 -> "confirm";
                    case 164 -> "cancel";
                    case 165 -> "cancel";
                    case 166 -> "manager";
                    case 167 -> "algorithm";
                    case 168 -> "SB";
                    case 169 -> "1";
                    case 170 -> "garage holder";
                    case 171 -> "details";
                    case 172 -> "0";
                    case 173 -> "cancel";
                    case 174 -> "cancel";
                    case 175 -> "manager";
                    case 176 -> "statistics";
                    case 177 -> "cancel";
                    default -> "quit";
                };
            }

            @Override
            public void println(String l) {
                switch (prints++) {
                    case 46 -> assertEquals("Order (Model A): orderTime=Day 0, 06:00, endTime=Day 0, 08:30, status=OnAssemblyLine}", l);
                    case 110, 149 -> assertEquals("Order (Model C): orderTime=Day 0, 19:30, endTime=Day 1, 09:00, status=Pending}", l);
                    case 150 -> assertEquals("Order (Model C): orderTime=Day 0, 19:30, endTime=Day 1, 10:00, status=Pending}", l);
                    case 387 -> assertEquals("Order (Model C): orderTime=Day 0, 19:30, endTime=Day 1, 09:00, status=Pending}", l);
                    case 388 -> assertEquals("Order (Model C): orderTime=Day 0, 19:30, endTime=Day 1, 10:00, status=Pending}", l);
                    case 389 -> assertEquals("Order (Model C): orderTime=Day 0, 19:30, endTime=Day 1, 11:00, status=Pending}", l);
                    case 390 -> assertEquals("Order (Model C): orderTime=Day 0, 19:30, endTime=Day 1, 12:10, status=Pending}", l);
                    case 391 -> assertEquals("Order (Model C): orderTime=Day 0, 19:30, endTime=Day 1, 13:20, status=Pending}", l);
                    case 392 -> assertEquals("Order (Model B): orderTime=Day 0, 19:30, endTime=Day 1, 14:30, status=Pending}", l);
                    case 393 -> assertEquals("Order (Model B): orderTime=Day 0, 19:30, endTime=Day 1, 15:40, status=Pending}", l);
                    case 502 -> assertEquals("Order (Model C): orderTime=Day 1, 06:00, endTime=Day 1, 16:40, status=Pending}", l);
                    case 508 -> assertEquals("Order (Model B): orderTime=Day 0, 19:30, endTime=Day 1, 14:30, status=Pending}", l);
                    case 520 -> assertEquals("0: Order (Model C): orderTime=Day 1, 06:00, endTime=Day 1, 14:20, status=Pending}", l);
                    case 526 -> assertEquals("6: Order (Model B): orderTime=Day 0, 19:30, endTime=Day 1, 15:30, status=Pending}", l);
                    case 528 -> assertEquals("Finished Orders:", l);
                    case 529 -> assertEquals("8: Order (Model A): orderTime=Day 0, 06:00, endTime=Day 0, 21:00, status=Finished}", l);
                    case 533 -> assertEquals("""
                            Last delay was 0 minutes, at Day 0, 06:00 minutes
                            Second last delay was null minutes, at null minutes
                            The median amount of delay was: 0 minutes, the average was 0.0 minutes
                            Yesterday, 1 orders got finished
                            The day before that: 0
                            The median amount of finished orders per day is: 1, the average is 1.0
                            """, l);
                }
            }

            @Override
            public void print(String s) {

            }

            @Override
            public void printf(String format, Object obj) {
                switch (prints++) {
                    case 154 -> assertEquals("", obj);
                    case 156 -> assertEquals("Order (Model A): orderTime=Day 0, 06:00, endTime=Day 0, 21:10, status=OnAssemblyLine}", obj);
                    case 157 -> assertEquals("", obj);
                    case 159 -> assertEquals("Task [Insert engine]: Standard 2l v4 (pending)", obj);
                    case 160 -> assertEquals("Task [Insert gearbox]: 6 speed manual (pending)", obj);
                    case 161 -> assertEquals("Nothing", obj);
                }
            }
        });
        var view = new AppTextView();
        view.start();
    }
}
