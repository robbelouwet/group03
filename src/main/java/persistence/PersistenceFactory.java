package persistence;

import lombok.Getter;
import lombok.Setter;


public class PersistenceFactory {
    /**
     * TODO:
     * Setter can only be called from test package
     */
    @Setter
    private static PersistenceFactory instance;
    @Getter
    private final CarOrderCatalog carOrderCatalog;
    @Getter
    private final CarRepository carRepository;

    public PersistenceFactory() {
        carOrderCatalog = new CarOrderCatalog();
        carRepository = new CarRepository();
    }

    public static PersistenceFactory getInstance() {
        if (instance == null) instance = new PersistenceFactory();
        return instance;
    }
}
