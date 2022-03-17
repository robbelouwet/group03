package domain.assembly;

import domain.order.CarOrder;
import domain.order.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WorkStationTest {
    private WorkStation station;
    AssemblyTask mockedTask1;
    AssemblyTask mockedTask2;

    @BeforeEach
    public void setup() {
        mockedTask1 = mock(AssemblyTask.class);
        mockedTask2 = mock(AssemblyTask.class);

        station = new WorkStation("TestStation", List.of(mockedTask1, mockedTask2));
    }

    @Test
    void hasCompleted_TasksFinished() {
        when(mockedTask1.isFinished()).thenReturn(true);
        when(mockedTask2.isFinished()).thenReturn(true);

        assertNull(station.getCarOrder());
        assertTrue(station.hasCompleted());
    }

    @Test
    void hasCompleted_OrderFinished() {
        // mock tasks as not done
        when(mockedTask1.isFinished()).thenReturn(false);
        when(mockedTask2.isFinished()).thenReturn(false);

        // Because we haven't set a currentOrder for this workstation, it succeeds
        assertTrue(station.hasCompleted());
    }

    @Test
    void finishCarOrder() {
        var mockedOrder = mock(CarOrder.class);
        var copiedOrder = mock(CarOrder.class);
        when(mockedOrder.copy()).thenReturn(copiedOrder);
        when(copiedOrder.getStatus()).thenReturn(OrderStatus.Finished);

        station.updateCurrentOrder(mockedOrder);
        assertNotNull(station.getCarOrder());

        var order = station.finishCarOrder();

        assertNull(station.getCarOrder());
        assertEquals(order.getStatus(), OrderStatus.Finished);
    }
}