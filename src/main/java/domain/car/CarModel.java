package domain.car;

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

    /**
     * @param name          The name of a {@code CarModel}
     * @param specification All possible options you can choose to mount on a {@code CarModel}
     */
    public CarModel(String name, CarModelSpecification specification) {
        this.name = name;
        this.modelSpecification = specification;
    }

    public boolean isValidInputData(Map<String, String> data) {
        return modelSpecification.isValidInputData(data);
    }
}
