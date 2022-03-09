package domain.car;

import domain.car.specification.CarModelSpecification;

public class CarModel {
    private final String name;
    private final CarModelSpecification specification;

    public CarModel(String name, CarModelSpecification specification) {
        this.name = name;
        this.specification = specification;
    }

    public String getModelSpecification(){
        return name;
    }

    @Override
    public CarModel clone(){
        return new CarModel(name, specification.clone());
    }
}
