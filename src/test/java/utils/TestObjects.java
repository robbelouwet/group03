package utils;

import domain.car.CarModel;
import domain.order.CarOrder;
import domain.scheduler.DateTime;
import persistence.CarCatalog;

public class TestObjects {
    private final static CarCatalog carCatalog = new CarCatalog();

    public static CarOrder getCarOrder() {
        var model = carCatalog.getModels().get(0);
        return getCarOrder(model);
    }

    public static CarOrder getCarOrder(CarModel model) {
        return getCarOrder(model, null);
    }

    public static CarOrder getCarOrder(CarModel model, DateTime dateTime) {
        var selector = model.getOptionSelector();
        for (var options : selector.getNotSelectedCategories().values()) {
            selector.selectOption(options.get(0));
        }
        var order =  new CarOrder(model, selector.getSelectedOptions());
        order.setOrderTime(dateTime);
        return order;
    }
}
