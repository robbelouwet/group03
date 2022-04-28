package app;

public class PerformTasksIntegrationTest {
    /*
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
                inputs.addAll(getInputsBasicOrder());
                inputs.addAll(getInputsBasicOrder());
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
                assertTrue(prints <= 67);
                switch (prints++) {
                    case 32, 63 -> assertEquals("Order (Ford Fiesta): startTime=Day 0, 6:0, endTime=Day 0, 9:0, status=Pending}", l);
                    case 64 -> assertEquals("Order (Ford Fiesta): startTime=Day 0, 6:0, endTime=Day 0, 10:0, status=Pending}", l);
                    case 67 -> assertEquals("Available workstations:", l);
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
    public void uiTest_withTwoOrdersMade_AdvancedOnce() {
        ConsoleReader.setInstance(new IConsoleReader() {
            int number = 0;
            int prints = 0;

            @Override
            public String ask(String str) {

                List<String> inputs = new ArrayList<>();

                // garage holder logs in, places two orders, logs out
                inputs.add("garage holder");
                inputs.addAll(getInputsBasicOrder());
                inputs.addAll(getInputsBasicOrder());
                inputs.add("cancel");

                // manager logs in, advances the assembly line, logs out
                inputs.add("manager");
                inputs.addAll(getInputsAdvance());

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
                // manager moved the assemblyLine ONCE,
                // so only the first workstation will be available, no prints after
                assertTrue(prints <= 77);
                switch (prints++) {
                    case 32, 63 -> assertEquals("Order (Ford Fiesta): startTime=Day 0, 6:0, endTime=Day 0, 9:0, status=Pending}", l);
                    case 64 -> assertEquals("Order (Ford Fiesta): startTime=Day 0, 6:0, endTime=Day 0, 10:0, status=Pending}", l);
                    case 76, 72 -> assertEquals("Available workstations:", l);
                    case 77, 73 -> assertEquals("- Workstation [Car Body Post]", l);
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
    public void uiTest_withTwoOrdersMade_AdvancedOnce_CompleteTasks() {
        ConsoleReader.setInstance(new IConsoleReader() {
            int number = 0;
            int prints = 0;

            @Override
            public String ask(String str) {
                List<String> inputs = new ArrayList<>();

                // garage holder logs in, places two orders, logs out
                inputs.add("garage holder");
                inputs.addAll(getInputsBasicOrder());
                inputs.addAll(getInputsBasicOrder());
                inputs.add("cancel");

                // manager logs in, advances the assembly line, logs out
                inputs.add("manager");
                inputs.addAll(getInputsAdvance());
                inputs.add("cancel");

                // mechanic logs in, finishes tasks, logs out
                inputs.add("mechanic");
                inputs.add("Car Body Post"); // see 2 tasks
                inputs.add("Assembly car body"); // see actions for task
                inputs.add("finish"); // see only second task

                inputs.add("cancel"); // workstation should still be available
                inputs.add("Car Body Post"); // see 1 tasks
                inputs.add("Paint car");
                inputs.add("finish"); // no more tasks
                inputs.add("cancel"); // no more available work stations
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
                assertTrue(prints <= 99);
                switch (prints++) {
                    case 69 -> assertEquals("Available workstations:", l);
                    case 70 -> assertEquals("- Workstation [Car Body Post]", l);
                    // Car Body Post
                    case 71 -> assertEquals("Available workstation tasks:", l);
                    case 72 -> assertEquals("-Task [Assembly car body]: is pending", l);
                    case 73 -> assertEquals("-Task [Paint car]: is pending", l);
                    // Assembly car body
                    case 74 -> assertEquals("Task [Assembly car body]: is pending", l);
                    case 75 -> assertEquals("Actions to complete this task:", l);
                    case 76 -> assertEquals("-Lift the body shell onto the chassis frame.", l);
                    case 77 -> assertEquals("-Bolt the shell and the frame together.", l);
                    // finish
                    case 78 -> assertEquals("Available workstation tasks:", l);
                    case 79 -> assertEquals("-Task [Paint car]: is pending", l);
                    // cancel (to see if workstation is still available)
                    case 80 -> assertEquals("Available workstations:", l);
                    case 81 -> assertEquals("- Workstation [Car Body Post]", l);
                    // Car Body Post
                    case 82 -> assertEquals("Available workstation tasks:", l);
                    case 83 -> assertEquals("-Task [Paint car]: is pending", l);
                    // Paint car
                    case 84 -> assertEquals("Task [Paint car]: is pending", l);
                    case 85 -> assertEquals("Actions to complete this task:", l);
                    case 86 -> assertEquals("-Make sure the car is clean, remove dust  if not.", l);
                    case 87 -> assertEquals("-Sand the body.", l);
                    case 88 -> assertEquals("-Clean it.", l);
                    case 89 -> assertEquals("-Tape the surfaces.", l);
                    case 90 -> assertEquals("-Paint the car.", l);
                    // finish
                    case 91 -> assertEquals("Available workstation tasks:", l);
                    // no more tasks, next print is
                    // cancel
                    case 92 -> assertEquals("Available workstations:", l);
                    // no more available workstations, this will be the last print, so print <= 99

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


    private List<String> getInputsBasicOrder() {
        return Arrays.asList(
                "order",
                "Ford Fiesta",
                "manual",
                "comfort",
                "red",
                "6 speed manual",
                "sedan",
                "standard",
                "leather black",
                "confirm"
        );
    }


    private List<String> getInputsAdvance() {
        return Arrays.asList(
                "yes",
                "30"
        );
    }

     */
}
