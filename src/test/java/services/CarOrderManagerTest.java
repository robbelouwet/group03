package services;

import domain.car.CarModel;
import domain.order.CarOrder;
import domain.order.OrderStatus;
import domain.scheduler.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.CarCatalog;
import persistence.CarOrderRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarOrderManagerTest {
    private ManagerStore managerStore;

    /**
     * This method 'resets the state of our domain'
     */
    @BeforeEach
    public void reset() {
        // mock a finished and not-finished order
        List<CarOrder> orders = new ArrayList<>();
        var model = CarCatalog.getModels().get(0);

        for (int i = 0; i < 2; i++) {
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
        orders.get(0).setStatus(OrderStatus.Finished);

        // Mock an OrderRepository
        var mockedRepo = mock(CarOrderRepository.class);
        when(mockedRepo.getOrders()).thenReturn(orders);

        // create testing instance of the ManagerStore
        when(mockedRepo.copy()).thenReturn(mockedRepo);
        managerStore = new ManagerStore(mockedRepo);
    }

    @Test
    void getPendingOrders() {
        var manager = managerStore.getCarOrderManager();
        var pendingOrders = manager.getPendingOrders();

        assertEquals(1, pendingOrders.size());
        assertTrue(pendingOrders.stream().noneMatch(CarOrder::isFinished));
    }

    @Test
    void getFinishedOrders() {
        var pendingOrders = managerStore.getCarOrderManager().getFinishedOrders();

        assertEquals(1, pendingOrders.size());
        assertTrue(pendingOrders.stream().allMatch(CarOrder::isFinished));
    }

    @Test
    void submitCarOrder() {
        var mockedModel = mock(CarModel.class);
        when(mockedModel.isValidInputData(anyMap())).thenReturn(true);
        managerStore.getCarOrderManager().selectModel(mockedModel);
        var order = managerStore.getCarOrderManager().submitCarOrder(new HashMap<>());

        assertNotNull(order);
    }

    @Test
    void selectModel() {
        var mockedModel = mock(CarModel.class);
        managerStore.getCarOrderManager().selectModel(mockedModel);
        var selectedModel = managerStore.getCarOrderManager().getSelectedModel();

        assertNotNull(selectedModel);
    }
}