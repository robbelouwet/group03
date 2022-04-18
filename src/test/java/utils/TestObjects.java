package utils;

import domain.order.CarOrder;
import domain.scheduler.DateTime;
import persistence.CarCatalog;

public class TestObjects {
    private final static CarCatalog carCatalog = new CarCatalog();

    public static CarOrder getCarOrder() {
        var model = carCatalog.getModels().get(0);
        var selector = model.getModelSpecification().getOptionSelector();
        for (var options : selector.getNotSelectedCategories().values()) {
            selector.selectOption(options.get(0));
        }
        return new CarOrder(model, selector.getSelectedOptions());
    }
}
