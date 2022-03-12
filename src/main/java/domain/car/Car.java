package domain.car;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An immutable object that represents a concrete instance of a car with all the selected options
 */
public class Car {
    private final Map<String, String> selections;
    @Getter
    private final CarModel model;

    /**
     * @param carModel   the model of this car
     * @param selections the concrete selection of options, a map which maps the option-name to its value
     */
    public Car(CarModel carModel, Map<String, String> selections) {
        var options = carModel.getModelSpecification().getOptions();
        if (!options.keySet().equals(selections.keySet()) || !selections.keySet().stream().allMatch(opt -> options.get(opt).contains(selections.get(opt)))) {
            throw new IllegalArgumentException("The data object does not match the modelspecification!");
        }
        this.model = carModel;
        this.selections = copySelections(selections);
    }

    private Map<String, String> copySelections(Map<String, String> selections) {
        return new HashMap<>(selections);
    }

    /**
     * @return the concrete selection of options, a map which maps the option-name to its value
     */
    public Map<String, String> getSelections() {
        return copySelections(selections);
    }
}
