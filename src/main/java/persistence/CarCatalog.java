package persistence;

import domain.car.CarModel;
import domain.car.CarModelSpecification;

import java.util.*;

public class CarCatalog {
    public static List<CarModel> getModels() {
        Map<String, List<String>> options = new HashMap<>();

        options.put("Body", List.of(new String[]{"sedan", "break"}));
        // TODO tijdelijk in comments, om makkelijker te kunnen testen
        /*
        options.put("Color", List.of(new String[]{"red", "blue", "black", "white"}));
        options.put("Engine", List.of(new String[]{"standard", "performance"}));
        options.put("Gearbox", List.of(new String[]{"6 speed manual", "5 speed automatic"}));
        options.put("Seats", List.of(new String[]{"leather black", "leather white", "vinyl grey"}));
        options.put("Airco", List.of(new String[]{"manual", "automatic"}));
        options.put("Wheels", List.of(new String[]{"comfort", "sports"}));

         */

        CarModelSpecification specification = new CarModelSpecification(options);
        return List.of(new CarModel[]{new CarModel("Model A", specification, 50), new CarModel("Model B", specification, 310), new CarModel("Model C", specification, 60)});
    }
}
