package persistence;

import lombok.Getter;
import lombok.Setter;


public class PersistenceFactory {
    @Getter
    @Setter
    /**
     * TODO:
     * Setter can only be called from test package
     */
    private static PersistenceFactory instance = new PersistenceFactory();
    @Getter
    private final CarOrderCatalog carOrderCatalog = new CarOrderCatalog();
    @Getter
    private final CarRepository carRepository = new CarRepository();
}
