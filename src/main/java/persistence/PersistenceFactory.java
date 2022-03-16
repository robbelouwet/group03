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
    private final CarOrderRepository carOrderCatalog;
    @Getter
    private final CarCatalog carRepository;

    public PersistenceFactory() {
        carOrderCatalog = new CarOrderRepository();
        carRepository = new CarCatalog();
    }

    public static PersistenceFactory getInstance() {
        if (instance == null) instance = new PersistenceFactory();
        return instance;
    }
}
