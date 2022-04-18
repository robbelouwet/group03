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
import utils.TestObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarControllerTest {
    private CarController controller;
    @BeforeEach
    public void setup() {
        var orders = new ArrayList<CarOrder>();
        for (int i = 0; i < 3; i++) {
            var order = TestObjects.getCarOrder();
            order.setOrderTime(new DateTime(2));
            orders.add(order);
        }
        for (int i = 0; i < 2; i++) {
            var order = TestObjects.getCarOrder();
            order.setOrderTime(new DateTime(2));
            order.setStatus(OrderStatus.Finished);
            orders.add(order);
        }
        var managerStore = new ManagerStore(new CarOrderRepository(orders));
        var controllerStore = new ControllerStore(managerStore);
        controller = controllerStore.getCarController();
    }

    @Test
    void showMainMenu() {
        var view = new IGarageHolderView() {

            @Override
            public boolean showOverview(List<String> pendingOrders, List<String> finishedOrders) {
                assertEquals(pendingOrders.size(), 3);
                assertEquals(finishedOrders.size(), 2);
                return false;
            }

            @Override
            public String showCarModels(List<String> models) {
                return null;
            }

            @Override
            public String showCarOption(String category, List<String> options) {
                return null;
            }

            @Override
            public boolean confirmOrder() {
                return false;
            }

            @Override
            public void showPredictedEndTime(DateTime endTime) {

            }
        };
        controller.setUi(view);
        controller.start();
    }

    @Test
    void showModels() {
        var view = new IGarageHolderView() {
            private int counter = 0;
            @Override
            public boolean showOverview(List<String> pendingOrders, List<String> finishedOrders) {
                return counter++ < 1;
            }

            @Override
            public String showCarModels(List<String> models) {
                assertEquals(3, models.size());
                assertEquals("Model A", models.get(0));
                assertEquals("Model B", models.get(1));
                assertEquals("Model C", models.get(2));
                return "Model A";
            }

            @Override
            public String showCarOption(String category, List<String> options) {
                return null;
            }

            @Override
            public boolean confirmOrder() {
                return false;
            }

            @Override
            public void showPredictedEndTime(DateTime endTime) {

            }
        };
        controller.setUi(view);
        controller.start();
    }

    @Test
    void submitOrder() {
        var view = new IGarageHolderView() {
            int overviewCounter = 0;
            @Override
            public boolean showOverview(List<String> pendingOrders, List<String> finishedOrders) {
                if (overviewCounter++ < 1) {
                    assertEquals(3, pendingOrders.size());
                    assertEquals(2, finishedOrders.size());
                    return true;
                }
                assertEquals(4, pendingOrders.size());
                assertEquals(2, finishedOrders.size());
                return false;
            }

            @Override
            public String showCarModels(List<String> models) {
                return models.get(0);
            }

            @Override
            public String showCarOption(String category, List<String> options) {
                return options.get(0);
            }

            @Override
            public boolean confirmOrder() {
                return true;
            }


            @Override
            public void showPredictedEndTime(DateTime endTime) {
                assertEquals(new DateTime(0, 12, 0), endTime);
            }
        };
        controller.setUi(view);
        controller.start();
    }
}
