package domain.car;

import lombok.Getter;

/**
 * @immutable
 */
public class CarModel {
    /**
     * @immutable
     */
    @Getter
    private final String name;
    /**
     * @immutable
     * @representationObject
     */
    @Getter
    private final CarModelSpecification modelSpecification;

    /**
     * @post | getName().equals(name) && getModelSpecification() == specification
     */
    public CarModel(String name, CarModelSpecification specification) {
        this.name = name;
        this.modelSpecification = specification;
    }
}
