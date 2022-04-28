package utils;

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
    }

    public static CarOrder getCarOrder(CarModel model) {
        return getCarOrder(model, null);
    }

    public static CarOrder getCarOrder(CarModel model, DateTime dateTime) {
        var selector = model.getOptionSelector();
        for (var options : selector.getNotSelectedCategories().values()) {
            selector.selectOption(options.get(0));
        }
        var order =  new CarOrder(model, selector.getSelectedOptions());
        order.setOrderTime(dateTime);
        return order;
    }

    public static Map<OptionCategory, List<Option>> getCarOptions(){
        Map<OptionCategory, List<Option>> options = new HashMap<>();

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
        return options;
    }

    public static List<CarOrder> getCarOrdersForAlgorithm(){
        var options = getCarOptions();
        CarModelSpecification specification = new CarModelSpecification(options);

        var body = new OptionCategory("Body");
        var color = new OptionCategory("Color");
        var engine = new OptionCategory("Engine");
        var gearbox = new OptionCategory("Gearbox");
        var seats = new OptionCategory("Seats");
        var airco = new OptionCategory("Airco");
        var wheels = new OptionCategory("Wheels");

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
                        new CarModel("Ford Fiesta", specification, 10),
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
                        new CarModel("Ford Fiesta", specification, 50),
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
                        new CarModel("Ford Fiesta", specification, 70),
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
}
