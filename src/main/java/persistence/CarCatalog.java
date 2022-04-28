package persistence;

import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.car.options.Option;
import domain.car.options.OptionCategory;
import domain.car.options.OptionCategoryRule;

import java.util.*;

public class CarCatalog {
    private final List<CarModel> carModels;

    public CarCatalog() {
        var spoiler = new OptionCategory("Spoiler");
        var low = new Option(spoiler, "Low");
        var high = new Option(spoiler, "High");
        var noSpoiler = new Option(spoiler, "None");

        var wheels = new OptionCategory("Wheels");
        var winter = new Option(wheels, "Winter");
        var comfort = new Option(wheels, "Comfort");
        var sports = new Option(wheels, "Sports");

        var airco = new OptionCategory("Airco");
        var manual = new Option(airco, "Manual");
        var automatic = new Option(airco, "Automatic");
        var noAirco = new Option(airco, "None");

        var seats = new OptionCategory("Seats");
        var lWhite = new Option(seats, "Leather white");
        var lBlack = new Option(seats, "Leather black");
        var vGrey = new Option(seats, "Vinyl grey");

        var gearbox = new OptionCategory("Gearbox");
        var sManual6 = new Option(gearbox, "6 speed manual");
        var sManual5 = new Option(gearbox, "5 speed manual");
        var sAutomatic5 = new Option(gearbox, "5 speed automatic");

        var engine = new OptionCategory("Engine");
        var standard = new Option(engine, "Standard 2l v4");
        var performance = new Option(engine, "Performance 2.5l v6");
        var ultra = new Option(engine, "Ultra 3l v8");

        var color = new OptionCategory("Color");
        var red = new Option(color, "Red");
        var blue = new Option(color, "Blue");
        var green = new Option(color, "Green");
        var yellow = new Option(color, "Yellow");
        var black = new Option(color, "Black");
        var white = new Option(color, "White");

        var body = new OptionCategory("Body");
        var sedan = new Option(body, "Sedan");
        var breakB = new Option(body, "Break");
        var sport = new Option(body, "Sport");

        Map<Option, Map<OptionCategory, List<Option>>> bodyRule = new LinkedHashMap<>();
        Map<OptionCategory, List<Option>> sportsRules = new LinkedHashMap<>();
        sportsRules.put(spoiler, List.of(low, high));  // If you select a sport body, a spoiler is also mandatory
        sportsRules.put(engine, List.of(performance, ultra));  // If you select a sport body, you must also select the performance or ultra engine.
        bodyRule.put(sport, sportsRules);
        body.setOptionCategoryRule(new OptionCategoryRule(bodyRule));

        Map<Option, Map<OptionCategory, List<Option>>> engineRule = new LinkedHashMap<>();
        Map<OptionCategory, List<Option>> ultraRules = new LinkedHashMap<>();
        ultraRules.put(airco, List.of(manual, noAirco));  // If you select the ultra engine, you can only fit the manual airco into your car.
        engineRule.put(ultra, ultraRules);
        engine.setOptionCategoryRule(new OptionCategoryRule(engineRule));

        Map<OptionCategory, List<Option>> optionsA = new LinkedHashMap<>();
        optionsA.put(body, List.of(sedan, breakB));
        optionsA.put(color, List.of(red, blue, black, white));
        optionsA.put(engine, List.of(standard, performance));
        optionsA.put(gearbox, List.of(sManual6, sManual5, sAutomatic5));
        optionsA.put(seats, List.of(lWhite, lBlack, vGrey));
        optionsA.put(airco, List.of(manual, automatic, noAirco));
        optionsA.put(wheels, List.of(winter, comfort, sports));
        optionsA.put(spoiler, List.of(noSpoiler));
        var modelASpec = new CarModelSpecification(optionsA);

        Map<OptionCategory, List<Option>> optionsB = new LinkedHashMap<>();
        optionsB.put(body, List.of(sedan, breakB, sport));
        optionsB.put(color, List.of(red, blue, green, yellow));
        optionsB.put(engine, List.of(standard, performance, ultra));
        optionsB.put(gearbox, List.of(sManual6, sAutomatic5));
        optionsB.put(seats, List.of(lWhite, lBlack, vGrey));
        optionsB.put(airco, List.of(manual, automatic, noAirco));
        optionsB.put(wheels, List.of(winter, comfort, sports));
        optionsB.put(spoiler, List.of(low, noSpoiler));
        var modelBSpec = new CarModelSpecification(optionsB);

        Map<OptionCategory, List<Option>> optionsC = new LinkedHashMap<>();
        optionsC.put(body, List.of(sport));
        optionsC.put(color, List.of(black, white));
        optionsC.put(engine, List.of(performance, ultra));
        optionsC.put(gearbox, List.of(sManual6));
        optionsC.put(seats, List.of(lWhite, lBlack));
        optionsC.put(airco, List.of(manual, automatic, noAirco));
        optionsC.put(wheels, List.of(winter, sports));
        optionsC.put(spoiler, List.of(high, low, noSpoiler));
        var modelCSpec = new CarModelSpecification(optionsC);

        carModels = List.of(new CarModel[]{new CarModel("Model A", modelASpec, 50), new CarModel("Model B", modelBSpec, 70), new CarModel("Model C", modelCSpec, 60)});
    }

    public List<CarModel> getModels() {
        return new ArrayList<>(carModels);
    }
}
