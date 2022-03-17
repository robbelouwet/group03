package services;

import domain.car.CarModel;
import domain.order.CarOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.CarCatalog;
import persistence.CarOrderRepository;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarOrderManagerTest {

    /**
     * This method 'resets the state of our domain'
     */
    @BeforeEach
    public void reset() {
        // mock a finished and not-finished order
        var mockedOrder1 = mock(CarOrder.class);
        var mockedOrder2 = mock(CarOrder.class);
        when(mockedOrder1.isFinished()).thenReturn(false);
        when(mockedOrder2.isFinished()).thenReturn(true);
        when(mockedOrder1.copy()).thenReturn(mockedOrder1);
        when(mockedOrder2.copy()).thenReturn(mockedOrder2);

        // Mock an OrderRepository
        var mockedRepo = mock(CarOrderRepository.class);
        when(mockedRepo.getOrders()).thenReturn(List.of(mockedOrder1, mockedOrder2));

        // create testing instance of the ManagerStore
        ManagerStore.getInstance().init(mockedRepo);
    }

    @Test
    void getPendingOrders() {
        var pendingOrders = ManagerStore.getInstance().getCarOrderManager().getPendingOrders();

        assertEquals(1, pendingOrders.size());
        assertTrue(pendingOrders.stream().noneMatch(CarOrder::isFinished));
    }

    @Test
    void getFinishedOrders() {
        var pendingOrders = ManagerStore.getInstance().getCarOrderManager().getFinishedOrders();

        assertEquals(1, pendingOrders.size());
        assertTrue(pendingOrders.stream().allMatch(CarOrder::isFinished));
    }

    @Test
    void submitCarOrder() {
        var mockedModel = mock(CarModel.class);
        when(mockedModel.isValidInputData(anyMap())).thenReturn(true);
        ManagerStore.getInstance().getCarOrderManager().selectModel(mockedModel);
        var order = ManagerStore.getInstance().getCarOrderManager().submitCarOrder(new HashMap<>());

        assertNotNull(order);
    }

    @Test
    void selectModel() {
        var mockedModel = mock(CarModel.class);
        ManagerStore.getInstance().getCarOrderManager().selectModel(mockedModel);
        var selectedModel = ManagerStore.getInstance().getCarOrderManager().getSelectedModel();

        assertNotNull(selectedModel);
    }
}