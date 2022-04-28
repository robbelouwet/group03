package app;

import app.ui.AppTextView;
import app.utils.ConsoleReader;
import app.utils.IConsoleReader;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PerformTasksIntegrationTest {

    @Test
    public void uiTest_withTwoOrdersMade_NothingOnTheAssemblyLine() {
        ConsoleReader.setInstance(new IConsoleReader() {
            int number = 0;
            int prints = 0;

            @Override
            public String ask(String str) {
                List<String> inputs = new ArrayList<>();

                // garage holder logs in, places two orders, logs out
                inputs.add("garage holder");
                inputs.addAll(getInputsBasicOrderForModelA());
                inputs.addAll(getInputsBasicOrderForModelC());
                inputs.add("cancel");

                // mechanic logs in, logs out
                inputs.add("mechanic");
                inputs.add("cancel");

                inputs.add("quit");

                if (number >= inputs.size()) {
                    return "quit";
                }

                return inputs.get(number++);
            }

            @Override
            public void println(String l) {
                // nothing on assemblyLine yet, so no print after "Available workstations:"
                assertTrue(prints <= 87);
                switch (prints++) {
                    // after the first order, order for model A is the only one
                    case 44 -> assertEquals("Predicted end time: Day 0, 08:30", l);
                    case 45 -> assertEquals("Pending orders:", l);
                    case 46 -> assertEquals("Order (Model A): orderTime=Day 0, 06:00, endTime=Day 0, 08:30, status=OnAssemblyLine}", l);
                    case 47 -> assertEquals("Finished orders:", l);

                    // after the second order
                    case 82 -> assertEquals("Predicted end time: Day 0, 09:50", l);
                    case 83 -> assertEquals("Pending orders:", l);
                    // endTime for order model A should now be delayed by 20 minutes (model C takes 10' longer/WS on average)
                    case 84 -> assertEquals("Order (Model A): orderTime=Day 0, 06:00, endTime=Day 0, 08:50, status=OnAssemblyLine}", l);
                    case 85 -> assertEquals("Order (Model C): orderTime=Day 0, 06:00, endTime=Day 0, 09:50, status=Pending}", l);
                    case 86 -> assertEquals("Finished orders:", l);
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
    public void uiTest_withTwoOrdersMade_AutomaticallyAdvancedOnce() {
        ConsoleReader.setInstance(new IConsoleReader() {
            int number = 0;
            int prints = 0;

            @Override
            public String ask(String str) {

                List<String> inputs = new ArrayList<>();

                // garage holder logs in, places two orders, logs out
                inputs.add("garage holder");
                inputs.addAll(getInputsBasicOrderForModelA());
                inputs.addAll(getInputsBasicOrderForModelC());
                inputs.add("cancel");

                // mechanic logs in, finishes all task at the first workstation
                // so the assemblyLine advances and the second order's status will be OnAssemblyLine
                inputs.add("mechanic");
                inputs.addAll(getInputsFinishingAllTasksAtFirstWorkStation());
                inputs.add("cancel");

                inputs.add("quit");

                if (number >= inputs.size()) {
                    return "quit";
                }
                return inputs.get(number++);
            }

            @Override
            public void println(String l) {
                // manager moved the assemblyLine ONCE,
                // so only the first workstation will be available, no prints after
                assertTrue(prints <= 111);
                switch (prints++) {
                    case 44 -> assertEquals("Predicted end time: Day 0, 08:30", l);
                    case 45 -> assertEquals("Pending orders:", l);
                    case 46 -> assertEquals("Order (Model A): orderTime=Day 0, 06:00, endTime=Day 0, 08:30, status=OnAssemblyLine}", l);
                    case 47 -> assertEquals("Finished orders:", l);

                    // after the second order
                    case 82 -> assertEquals("Predicted end time: Day 0, 09:50", l);
                    case 83 -> assertEquals("Pending orders:", l);
                    // endTime for order model A should now be delayed by 20 minutes (model C takes 10' longer/WS on average)
                    case 84 -> assertEquals("Order (Model A): orderTime=Day 0, 06:00, endTime=Day 0, 08:50, status=OnAssemblyLine}", l);
                    case 85 -> assertEquals("Order (Model C): orderTime=Day 0, 06:00, endTime=Day 0, 09:50, status=Pending}", l);
                    case 86 -> assertEquals("Finished orders:", l);

                    // before finishing tasks at WorkStation [Car Body Post]
                    case 88 -> assertEquals("Available workstations:", l);
                    case 89 -> assertEquals("- Workstation [Car Body Post]", l);
                    case 90 -> assertEquals("Available workstation tasks:", l);
                    case 91 -> assertEquals("-Task [Assembly car body]: Sedan (pending)", l);
                    case 92 -> assertEquals("-Task [Paint car]: Red (pending)", l);

                    // mechanic selects first task and finishes it in 20 minutes
                    case 93 -> assertEquals("Task [Assembly car body]: Sedan (pending)", l);

                    case 97 -> assertEquals("Available workstation tasks:", l);
                    case 98 -> assertEquals("-Task [Paint car]: Red (pending)", l);

                    // mechanic selects second task and finishes it in 20 minutes
                    case 99 -> assertEquals("Task [Paint car]: Red (pending)", l);

                    case 106 -> assertEquals("Available workstation tasks:", l);

                    // after all tasks have been completed, the assemblyline advances automatically
                    // and the tasks for the next order are now pending at the workstation
                    case 107 -> assertEquals("-Task [Assembly car body]: Sport (pending)", l);
                    case 108 -> assertEquals("-Task [Paint car]: Black (pending)", l);

                    // the mechanic goes back to the selectWorkstation menu
                    // now there are 2 Workstations to work on
                    case 109 -> assertEquals("Available workstations:", l);
                    case 110 -> assertEquals("- Workstation [Car Body Post]", l);
                    case 111 -> assertEquals("- Workstation [Drivetrain Post]", l);
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


    private List<String> getInputsBasicOrderForModelA() {
        return Arrays.asList(
                "order",
                "order",
                "Model A",
                "Sedan",
                "Red",
                "Standard 2l v4",
                "6 speed manual",
                "Vinyl grey",
                "Manual",
                "Winter",
                "None",
                "confirm"
        );
    }

    private List<String> getInputsBasicOrderForModelC() {
        return Arrays.asList(
                "order",
                "order",
                "Model C",
                "Sport",
                "Black",
                "Ultra 3l v8",
                "6 speed manual",
                "Leather white",
                "Manual",
                "Winter",
                "Low",
                "confirm"
        );
    }

    private List<String> getInputsFinishingAllTasksAtFirstWorkStation() {
        return Arrays.asList(
                "tasks",
                "Car Body Post",
                "Assembly car body",
                "finish",
                "20",
                "Paint car",
                "finish",
                "20",
                "cancel"

        );
    }


}
