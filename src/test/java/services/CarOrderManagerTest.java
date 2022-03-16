package services;

import domain.car.CarModel;
import domain.order.CarOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.CarOrderRepository;
import persistence.PersistenceFactory;

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

        // mock the factories
        var mockedPFactory = mock(PersistenceFactory.class);
        var mockedCatalog = mock(CarOrderRepository.class);
        when(mockedPFactory.getCarOrderCatalog()).thenReturn(mockedCatalog);
        when(mockedCatalog.getOrders()).thenReturn(List.of(mockedOrder1, mockedOrder2));

        // inject the mocked/testing instances of the factories
        PersistenceFactory.setInstance(mockedPFactory);
        ManagerFactory.setInstance(new ManagerFactory());
    }

    @Test
    void getPendingOrders() {
        var pendingOrders = ManagerFactory.getInstance().getCarOrderManager().getPendingOrders();

        assertEquals(1, pendingOrders.size());
        assertTrue(pendingOrders.stream().noneMatch(CarOrder::isFinished));
    }

    @Test
    void getFinishedOrders() {
        var pendingOrders = ManagerFactory.getInstance().getCarOrderManager().getFinishedOrders();

        assertEquals(1, pendingOrders.size());
        assertTrue(pendingOrders.stream().allMatch(CarOrder::isFinished));
    }

    @Test
    void submitCarOrder() {
        var mockedModel = mock(CarModel.class);
        when(mockedModel.isValidInputData(anyMap())).thenReturn(true);
        ManagerFactory.getInstance().getCarOrderManager().selectModel(mockedModel);
        var order = ManagerFactory.getInstance().getCarOrderManager().submitCarOrder(new HashMap<>());

        assertNotNull(order);
    }

    @Test
    void selectModel() {
        var mockedModel = mock(CarModel.class);
        ManagerFactory.getInstance().getCarOrderManager().selectModel(mockedModel);
        var selectedModel = ManagerFactory.getInstance().getCarOrderManager().getSelectedModel();

        assertNotNull(selectedModel);
    }
}