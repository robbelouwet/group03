package utils;

import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;
import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.car.options.Option;
import domain.car.options.OptionCategory;
import domain.order.CarOrder;
import domain.order.OrderStatus;
import domain.scheduler.DateTime;
import persistence.CarCatalog;

import java.util.*;

public class TestObjects {
    private final static CarCatalog carCatalog = new CarCatalog();

    public static CarOrder getCarOrder() {
        var model = carCatalog.getModels().get(0);
        return getCarOrder(model);
        // this results in a car order for following model
        // Model A
        // body: Sedan
        // color: Red
        // engine: Standard 2l v4
        // gearbox: 6 speed manual
        // seats: Leather white
        // airco: Manual
        // wheels: Winter
        // spoiler: None
    }

    public static CarOrder getCarOrder(CarModel model) {
        return getCarOrder(model, null);
    }

    public static CarOrder getCarOrder(CarModel model, DateTime dateTime) {
        var selector = model.getOptionSelector();
        for (var options : selector.getNotSelectedCategories().values()) {
            selector.selectOption(options.get(0));
        }
        var order = new CarOrder(model, selector.getSelectedOptions());
        order.setOrderTime(dateTime);
        return order;
    }

    public static Map<OptionCategory, List<Option>> getCarOptions() {
        Map<OptionCategory, List<Option>> options = new HashMap<>();

        var body = new OptionCategory("Body");
        var color = new OptionCategory("Color");
        var engine = new OptionCategory("Engine");
        var gearbox = new OptionCategory("Gearbox");
        var seats = new OptionCategory("Seats");
        var airco = new OptionCategory("Airco");
        var wheels = new OptionCategory("Wheels");
        var spoiler = new OptionCategory("Spoiler");

        options.put(body, List.of(new Option(body, "Sedan"), new Option(body, "Break")));
        options.put(color, List.of(
                new Option(color, "Red"),
                new Option(color, "Blue"),
                new Option(color, "Green"),
                new Option(color, "Yellow"),
                new Option(color, "Black"),
                new Option(color, "White")));
        options.put(engine, List.of(new Option(engine, "Standard 2l v4"), new Option(engine, "Performance 2.5l v6"), new Option(engine, "Ultra 3l v8")));
        options.put(gearbox, List.of(new Option(gearbox, "6 speed manual"), new Option(gearbox, "5 speed manual"), new Option(gearbox, "5 speed automatic")));
        options.put(seats, List.of(
                new Option(seats, "Leather black"),
                new Option(seats, "Leather white"),
                new Option(seats, "Vinyl grey")));
        options.put(airco, List.of(new Option(airco, "Manual"), new Option(airco, "Automatic"), new Option(airco, "None")));
        options.put(wheels, List.of(new Option(wheels, "Comfort"), new Option(wheels, "Sports"), new Option(wheels, "Winter")));
        options.put(spoiler, List.of(new Option(spoiler, "Low"), new Option(spoiler, "High"), new Option(spoiler, "None")));
        return options;
    }

    public static List<CarOrder> getCarOrdersForAlgorithm() {
        var options = getCarOptions();
        CarModelSpecification specification = new CarModelSpecification(options);

        var body = new OptionCategory("Body");
        var color = new OptionCategory("Color");
        var engine = new OptionCategory("Engine");
        var gearbox = new OptionCategory("Gearbox");
        var seats = new OptionCategory("Seats");
        var airco = new OptionCategory("Airco");
        var wheels = new OptionCategory("Wheels");
        var spoiler = new OptionCategory("Spoiler");

        return new ArrayList<>(List.of(
                new CarOrder(
                        new CarModel("Ford Fiesta", specification, 60),
                        Map.of(
                                new OptionCategory("Body"), new Option(body, "Sedan"),
                                new OptionCategory("Color"), new Option(color, "Red"),
                                new OptionCategory("Engine"), new Option(engine, "Standard 2l v4"),
                                new OptionCategory("Gearbox"), new Option(gearbox, "6 speed manual"),
                                new OptionCategory("Seats"), new Option(seats, "Leather black"),
                                new OptionCategory("Airco"), new Option(airco, "Manual"),
                                new OptionCategory("Wheels"), new Option(wheels, "Comfort"),
                                new OptionCategory("Spoiler"), new Option(spoiler, "None")
                        )
                ),
                new CarOrder(
                        new CarModel("Ford Fiesta", specification, 60),
                        Map.of(
                                new OptionCategory("Body"), new Option(body, "Sedan"),
                                new OptionCategory("Color"), new Option(color, "Red"),
                                new OptionCategory("Engine"), new Option(engine, "Standard 2l v4"),
                                new OptionCategory("Gearbox"), new Option(gearbox, "6 speed manual"),
                                new OptionCategory("Seats"), new Option(seats, "Leather black"),
                                new OptionCategory("Airco"), new Option(airco, "Manual"),
                                new OptionCategory("Wheels"), new Option(wheels, "Comfort"),
                                new OptionCategory("Spoiler"), new Option(spoiler, "None")
                        )
                ),
                new CarOrder(
                        new CarModel("Ford Fiesta", specification, 10),
                        Map.of(
                                new OptionCategory("Body"), new Option(body, "Sedan"),
                                new OptionCategory("Color"), new Option(color, "Red"),
                                new OptionCategory("Engine"), new Option(engine, "Standard 2l v4"),
                                new OptionCategory("Gearbox"), new Option(gearbox, "6 speed manual"),
                                new OptionCategory("Seats"), new Option(seats, "Leather black"),
                                new OptionCategory("Airco"), new Option(airco, "Manual"),
                                new OptionCategory("Wheels"), new Option(wheels, "Comfort"),
                                new OptionCategory("Spoiler"), new Option(spoiler, "None")
                        )
                ),
                new CarOrder(
                        new CarModel("Ford Fiesta", specification, 60),
                        Map.of(
                                new OptionCategory("Body"), new Option(body, "Sedan"),
                                new OptionCategory("Color"), new Option(color, "Black"),
                                new OptionCategory("Engine"), new Option(engine, "Standard 2l v4"),
                                new OptionCategory("Gearbox"), new Option(gearbox, "6 speed manual"),
                                new OptionCategory("Seats"), new Option(seats, "Leather black"),
                                new OptionCategory("Airco"), new Option(airco, "Manual"),
                                new OptionCategory("Wheels"), new Option(wheels, "Comfort"),
                                new OptionCategory("Spoiler"), new Option(spoiler, "None")
                        )
                ),
                new CarOrder(
                        new CarModel("Ford Fiesta", specification, 50),
                        Map.of(
                                new OptionCategory("Body"), new Option(body, "Sedan"),
                                new OptionCategory("Color"), new Option(color, "Black"),
                                new OptionCategory("Engine"), new Option(engine, "Standard 2l v4"),
                                new OptionCategory("Gearbox"), new Option(gearbox, "6 speed manual"),
                                new OptionCategory("Seats"), new Option(seats, "Leather black"),
                                new OptionCategory("Airco"), new Option(airco, "Manual"),
                                new OptionCategory("Wheels"), new Option(wheels, "Comfort"),
                                new OptionCategory("Spoiler"), new Option(spoiler, "None")
                        )
                ),
                new CarOrder(
                        new CarModel("Ford Fiesta", specification, 70),
                        Map.of(
                                new OptionCategory("Body"), new Option(body, "Sedan"),
                                new OptionCategory("Color"), new Option(color, "Black"),
                                new OptionCategory("Engine"), new Option(engine, "Standard 2l v4"),
                                new OptionCategory("Gearbox"), new Option(gearbox, "6 speed manual"),
                                new OptionCategory("Seats"), new Option(seats, "Leather black"),
                                new OptionCategory("Airco"), new Option(airco, "Manual"),
                                new OptionCategory("Wheels"), new Option(wheels, "Comfort"),
                                new OptionCategory("Spoiler"), new Option(spoiler, "None")
                        )
                )
        ));
    }

    public static LinkedList<WorkStation> defaultAssemblyLine() {
        LinkedList<WorkStation> workStations = new LinkedList<>();

        List<String> actions1 = new ArrayList<>();
        actions1.add("Lift the body shell onto the chassis frame.");
        actions1.add("Bolt the shell and the frame together.");
        AssemblyTask task1 = new AssemblyTask("Assembly car body", actions1, new OptionCategory("Body"));

        List<String> actions2 = new ArrayList<>();
        actions2.add("Make sure the car is clean, remove dust  if not.");
        actions2.add("Sand the body.");
        actions2.add("Clean it.");
        actions2.add("Tape the surfaces.");
        actions2.add("Paint the car.");
        AssemblyTask task2 = new AssemblyTask("Paint car", actions2, new OptionCategory("Color"));

        List<String> actions3 = new ArrayList<>();
        actions3.add("Using the hoist, slowly lower the new motor into position.");
        actions3.add("Connect it to the transmission.");
        actions3.add("Reinstall the vacuum and fuel lines, and the throttle cables and accessories.");
        actions3.add("Reinstall the air intake manifold, battery, and fan belts.");
        actions3.add("Refill all of the fluids.");
        AssemblyTask task3 = new AssemblyTask("Insert engine", actions3, new OptionCategory("Engine"));

        List<String> actions4 = new ArrayList<>();
        actions4.add("Bolt the gearbox.");
        actions4.add("Connect the starter.");
        actions4.add("Connect the driveshaft.");
        actions4.add("Refill the transmission fluid.");
        actions4.add("Connect the exhaust.");
        actions4.add("Connect the shifter.");
        actions4.add("Connect the battery.");
        AssemblyTask task4 = new AssemblyTask("Insert gearbox", actions4, new OptionCategory("Gearbox"));

        List<String> actions5 = new ArrayList<>();
        actions5.add("Install the seat belt components.");
        actions5.add("Attach the bolts for each seat.");
        AssemblyTask task5 = new AssemblyTask("Install seats", actions5, new OptionCategory("Seats"));

        List<String> actions6 = new ArrayList<>();
        actions6.add("Install the air.");
        actions6.add("Install the co.");
        AssemblyTask task6 = new AssemblyTask("Install airco", actions6, new OptionCategory("Airco"));

        List<String> actions7 = new ArrayList<>();
        actions7.add("Lift the car.");
        actions7.add("Set the wheels on the studs.");
        actions7.add("Put the lug nuts on.");
        actions7.add("Put on the hubcap.");
        AssemblyTask task7 = new AssemblyTask("Mount wheels", actions7, new OptionCategory("Wheels"));

        List<String> actions8 = new ArrayList<>();
        actions8.add("Add spoiler.");
        AssemblyTask task8 = new AssemblyTask("Add spoiler", actions8, new OptionCategory("Spoiler"));

        WorkStation ws1 = new WorkStation("Car Body Post",
                new ArrayList<>(List.of(task1, task2)));
        WorkStation ws2 = new WorkStation("Drivetrain Post",
                new ArrayList<>(List.of(task3, task4)));
        WorkStation ws3 = new WorkStation("Accessories Post",
                new ArrayList<>(List.of(task5, task6, task7, task8)));

        workStations.add(ws1);
        workStations.add(ws2);
        workStations.add(ws3);

        return workStations;
    }

    public static List<CarOrder> getTestCarsForAlgorithm() {
        Map<OptionCategory, List<Option>> options = new LinkedHashMap<>();

        var body = new OptionCategory("Body");
        var color = new OptionCategory("Color");
        var engine = new OptionCategory("Engine");
        var gearbox = new OptionCategory("Gearbox");
        var seats = new OptionCategory("Seats");
        var airco = new OptionCategory("Airco");
        var wheels = new OptionCategory("Wheels");

        options.put(body, List.of(new Option(body, "sedan"), new Option(body, "break")));
        options.put(color, List.of(
                new Option(color, "red"),
                new Option(color, "blue"),
                new Option(color, "black"),
                new Option(color, "white")));
        options.put(engine, List.of(new Option(engine, "standard"), new Option(engine, "performance")));
        options.put(gearbox, List.of(new Option(gearbox, "6 speed manual"), new Option(gearbox, "5 speed automatic")));
        options.put(seats, List.of(
                new Option(seats, "leather black"),
                new Option(seats, "leather white"),
                new Option(seats, "vinyl grey")));
        options.put(airco, List.of(new Option(airco, "manual"), new Option(airco, "automatic")));
        options.put(wheels, List.of(new Option(wheels, "comfort"), new Option(wheels, "sports")));

        CarModelSpecification specification = new CarModelSpecification(options);


        return new ArrayList<>(List.of(
                new CarOrder(
                        new CarModel("Ford Fiesta", specification, 60),
                        Map.of(
                                new OptionCategory("Body"), new Option(body, "sedan"),
                                new OptionCategory("Color"), new Option(color, "red"),
                                new OptionCategory("Engine"), new Option(engine, "standard"),
                                new OptionCategory("Gearbox"), new Option(gearbox, "6 speed manual"),
                                new OptionCategory("Seats"), new Option(seats, "leather black"),
                                new OptionCategory("Airco"), new Option(airco, "manual"),
                                new OptionCategory("Wheels"), new Option(wheels, "comfort")
                        )
                ),
                new CarOrder(
                        new CarModel("Ford Fiesta", specification, 60),
                        Map.of(
                                new OptionCategory("Body"), new Option(body, "sedan"),
                                new OptionCategory("Color"), new Option(color, "red"),
                                new OptionCategory("Engine"), new Option(engine, "standard"),
                                new OptionCategory("Gearbox"), new Option(gearbox, "6 speed manual"),
                                new OptionCategory("Seats"), new Option(seats, "leather black"),
                                new OptionCategory("Airco"), new Option(airco, "manual"),
                                new OptionCategory("Wheels"), new Option(wheels, "comfort")
                        )
                ),
                new CarOrder(
                        new CarModel("Ford Fiesta", specification, 60),
                        Map.of(
                                new OptionCategory("Body"), new Option(body, "sedan"),
                                new OptionCategory("Color"), new Option(color, "red"),
                                new OptionCategory("Engine"), new Option(engine, "standard"),
                                new OptionCategory("Gearbox"), new Option(gearbox, "6 speed manual"),
                                new OptionCategory("Seats"), new Option(seats, "leather black"),
                                new OptionCategory("Airco"), new Option(airco, "manual"),
                                new OptionCategory("Wheels"), new Option(wheels, "comfort")
                        )
                ),
                new CarOrder(
                        new CarModel("Ford Fiesta", specification, 60),
                        Map.of(
                                new OptionCategory("Body"), new Option(body, "sedan"),
                                new OptionCategory("Color"), new Option(color, "black"),
                                new OptionCategory("Engine"), new Option(engine, "standard"),
                                new OptionCategory("Gearbox"), new Option(gearbox, "6 speed manual"),
                                new OptionCategory("Seats"), new Option(seats, "leather black"),
                                new OptionCategory("Airco"), new Option(airco, "manual"),
                                new OptionCategory("Wheels"), new Option(wheels, "comfort")
                        )
                ),
                new CarOrder(
                        new CarModel("Ford Fiesta", specification, 60),
                        Map.of(
                                new OptionCategory("Body"), new Option(body, "sedan"),
                                new OptionCategory("Color"), new Option(color, "black"),
                                new OptionCategory("Engine"), new Option(engine, "standard"),
                                new OptionCategory("Gearbox"), new Option(gearbox, "6 speed manual"),
                                new OptionCategory("Seats"), new Option(seats, "leather black"),
                                new OptionCategory("Airco"), new Option(airco, "manual"),
                                new OptionCategory("Wheels"), new Option(wheels, "comfort")
                        )
                ),
                new CarOrder(
                        new CarModel("Ford Fiesta", specification, 60),
                        Map.of(
                                new OptionCategory("Body"), new Option(body, "sedan"),
                                new OptionCategory("Color"), new Option(color, "black"),
                                new OptionCategory("Engine"), new Option(engine, "standard"),
                                new OptionCategory("Gearbox"), new Option(gearbox, "6 speed manual"),
                                new OptionCategory("Seats"), new Option(seats, "leather black"),
                                new OptionCategory("Airco"), new Option(airco, "manual"),
                                new OptionCategory("Wheels"), new Option(wheels, "comfort")
                        )
                )
        ));
    }

    public static List<CarOrder> delayedTestOrders() {
        var orders = getTestCarsForAlgorithm();

        var delays = Arrays.asList(0L, 120L, 60L, 60L, 120L, 60L);
        var startTimes = Arrays.asList(2 * 60 * 24L,
                60 * 24L,
                60 * 24L,
                60 * 24L,
                2 * 60 * 24L,
                2 * 60 * 24L);

        for (int i = 0; i < orders.size(); i++) {
            orders.get(i).setStatus(OrderStatus.Finished);
            orders.get(i).setOrderTime(new DateTime(startTimes.get(i)));
            var startTime = new DateTime(
                    startTimes.get(i) + delays.get(i)
            );
            orders.get(i).setStartTime(startTime);
            orders.get(i).setEndTime(startTime.addTime(60 * 3));
        }

        return orders;
    }
}
