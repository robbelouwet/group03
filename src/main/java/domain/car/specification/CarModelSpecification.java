package domain.car.specification;

import domain.car.options.Option;

import java.util.List;

public class CarModelSpecification {
    private List<Option<Selection>> options;

    @Override
    public CarModelSpecification clone(){
        return new CarModelSpecification();
    }
}
