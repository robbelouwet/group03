package app.controllers;

import app.ui.interfaces.IGarageHolderView;
import domain.order.CarOrder;
import domain.order.OrderStatus;
import domain.scheduler.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.CarCatalog;
import persistence.CarOrderRepository;
import services.ManagerStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarControllerTest {
    @BeforeEach
    public void setup() {
        var model = CarCatalog.getModels().get(0);
        var orders = new ArrayList<CarOrder>();
        for (int i = 0; i < 3; i++) {
            orders.add(new CarOrder(new DateTime(2), model, new HashMap<>() {{
                put("Body", "break");
                put("Color", "white");
                put("Engine", "performance");
                put("Gearbox", "5 speed automatic");
                put("Seats", "vinyl grey");
                put("Airco", "automatic");
                put("Wheels", "sports");
            }}));
        }
        for (int i = 0; i < 2; i++) {
            var order = new CarOrder(new DateTime(2), model, new HashMap<>() {{
                put("Body", "break");
                put("Color", "white");
                put("Engine", "performance");
                put("Gearbox", "5 speed automatic");
                put("Seats", "vinyl grey");
                put("Airco", "automatic");
                put("Wheels", "sports");
            }});
            order.setStatus(OrderStatus.Finished);
            orders.add(order);
        }

        ManagerStore.getInstance().init(new CarOrderRepository(orders));
    }

    @Test
    void showMainMenu() {
        var view = new IGarageHolderView() {

            @Override
            public void showOverview(List<String> pendingOrders, List<String> finishedOrders) {
                assertEquals(pendingOrders.size(), 3);
                assertEquals(finishedOrders.size(), 2);
            }

            @Override
            public void showCarModels(List<String> models) {

            }

            @Override
            public void showCarForm(Map<String, List<String>> options) {

            }

            @Override
            public void showPredictedEndTime(DateTime endTime) {

            }
        };
        var controller = new CarController(view);
        controller.showMainMenu();
    }

    @Test
    void showModels() {
        var view = new IGarageHolderView() {

            @Override
            public void showOverview(List<String> pendingOrders, List<String> finishedOrders) {
            }

            @Override
            public void showCarModels(List<String> models) {
                assertEquals(1, models.size());
                assertEquals("Ford Fiesta", models.get(0));
            }

            @Override
            public void showCarForm(Map<String, List<String>> options) {

            }

            @Override
            public void showPredictedEndTime(DateTime endTime) {

            }
        };
        var controller = new CarController(view);
        controller.showModels();
    }

    @Test
    void submitOrder() {
        var view = new IGarageHolderView() {
            final CarController controller = new CarController(this);

            @Override
            public void showOverview(List<String> pendingOrders, List<String> finishedOrders) {
                assertEquals(4, pendingOrders.size());
                assertEquals(2, finishedOrders.size());
            }

            @Override
            public void showCarModels(List<String> models) {
                controller.selectModel(models.get(0));
            }

            @Override
            public void showCarForm(Map<String, List<String>> options) {
                Map<String, String> selection = new HashMap<>();
                for (var option : options.keySet()) {
                    selection.put(option, options.get(option).get(0));
                }
                controller.submitCarOrder(selection);
            }

            @Override
            public void showPredictedEndTime(DateTime endTime) {
                assertEquals(new DateTime(0, 12, 0), endTime);
                controller.showMainMenu();
            }
        };
        view.controller.showModels();
    }
}
