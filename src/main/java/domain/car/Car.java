package domain.car;

import domain.car.specification.CarModelSpecification;

import java.util.List;
import java.util.stream.Collectors;

public class Car {
    private List<CarModelSpecification> specifications;

    @Override
    public Car clone() {
        //TODO
        return new Car();
    }

    public List<CarModelSpecification> getSpecifications() {
        return specifications.stream()
                .map(CarModelSpecification::clone)
                .collect(Collectors.toList());
    }
}
