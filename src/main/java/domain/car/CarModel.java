package domain.car;

import domain.car.options.Option;
import domain.car.options.OptionCategory;
import lombok.Getter;

import java.util.Map;

/**
 * An immutable object that represents a car model
 */
public class CarModel {
    @Getter
    private final String name;

    @Getter
    private final CarModelSpecification modelSpecification;

    @Getter
    private final long expectedMinutesPerWorkStation;

    /**
     * @param name          The name of a {@code CarModel}
     * @param specification All possible options you can choose to mount on a {@code CarModel}
     */
    public CarModel(String name, CarModelSpecification specification, long expectedMinutesPerWorkStation) {
        this.name = name;
        this.modelSpecification = specification;
        this.expectedMinutesPerWorkStation = expectedMinutesPerWorkStation;
    }

    public boolean isValidInputData(Map<OptionCategory, Option> selections) {
        return modelSpecification.isValidInputData(selections);
    }
}
