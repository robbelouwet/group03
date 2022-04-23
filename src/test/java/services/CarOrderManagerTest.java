package services;

import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.car.options.OptionSelector;
import domain.order.CarOrder;
import domain.order.OrderStatus;
import domain.scheduler.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.CarCatalog;
import persistence.CarOrderRepository;
import utils.TestObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

        for (int i = 0; i < 2; i++) {
            orders.add(TestObjects.getCarOrder());
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
    void submitCarOrderFails() {
        var mockedModel = mock(CarModel.class);
        var mockedSelector = mock(OptionSelector.class);
        when(mockedSelector.getSelectedOptions()).thenReturn(new LinkedHashMap<>());
        when(mockedModel.getOptionSelector()).thenReturn(mockedSelector);
        when(mockedModel.isValidInputData(anyMap())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> managerStore.getCarOrderManager().submitCarOrder(mockedModel, mockedSelector));
    }

    @Test
    void submitCarOrderSuccess() {
        var mockedModel = mock(CarModel.class);
        var mockedSelector = mock(OptionSelector.class);
        when(mockedSelector.getSelectedOptions()).thenReturn(new LinkedHashMap<>());

        when(mockedModel.getOptionSelector()).thenReturn(mockedSelector);
        when(mockedModel.isValidInputData(anyMap())).thenReturn(true);
        assertNotNull(managerStore.getCarOrderManager().submitCarOrder(mockedModel, mockedSelector));
    }
}