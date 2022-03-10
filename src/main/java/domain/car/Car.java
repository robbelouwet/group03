package domain.car;

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Car {
    @Getter
    private final Map<String, String> selections;
    @Getter
    private final CarModel model;

    public Car(CarModel carModel, Map<String, String> selections) {
        this.model = carModel;
        this.selections = selections; // TODO clone this map
    }
}
