package domain.car;

import lombok.Getter;

/**
 * An immutable object that represents a car model
 */
public class CarModel {
    @Getter
    private final String name;

    @Getter
    private final CarModelSpecification modelSpecification;

    public CarModel(String name, CarModelSpecification specification) {
        this.name = name;
        this.modelSpecification = specification;
    }
}
