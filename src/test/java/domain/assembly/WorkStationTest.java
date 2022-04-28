package domain.assembly;

import domain.car.options.Option;
import domain.car.options.OptionCategory;
import domain.order.CarOrder;
import domain.order.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WorkStationTest {
    private WorkStation station;
    AssemblyTask mockedTask1;
    AssemblyTask mockedTask2;
    AssemblyTask mockedTask3;
    CarOrder mockedCarOrder;

    @BeforeEach
    public void setup() {
        mockedTask1 = mock(AssemblyTask.class);
        mockedTask2 = mock(AssemblyTask.class);
        mockedTask3 = mock(AssemblyTask.class);

        mockedCarOrder = mock(CarOrder.class);

        station = new WorkStation("TestStation", List.of(mockedTask1, mockedTask2));
        station.updateCurrentOrder(mockedCarOrder);
    }

    @Test
    void getPendingTasks() {
        station = new WorkStation("TestWorkStation", List.of(mockedTask1, mockedTask2, mockedTask3));
        station.updateCurrentOrder(mockedCarOrder);
        when(mockedTask1.isFinished(mockedCarOrder)).thenReturn(true);
        when(mockedTask2.isFinished(mockedCarOrder)).thenReturn(true);
        when(mockedTask3.isFinished(mockedCarOrder)).thenReturn(false);

        assertEquals(List.of(mockedTask3), station.getPendingTasks());
    }

    @Test
    void getPendingTasks_whenWorkStationHasNoCurrentOrder_returnsEmptyList() {
        station = new WorkStation("TestWorkStation", List.of(mockedTask1, mockedTask2, mockedTask3));
        when(mockedTask1.isFinished(mockedCarOrder)).thenReturn(true);
        when(mockedTask2.isFinished(mockedCarOrder)).thenReturn(true);
        when(mockedTask3.isFinished(mockedCarOrder)).thenReturn(false);

        assertEquals(List.of(), station.getPendingTasks());
    }

    @Test
    void getFinishedTasks() {
        station = new WorkStation("TestWorkStation", List.of(mockedTask1, mockedTask2, mockedTask3));
        station.updateCurrentOrder(mockedCarOrder);
        when(mockedTask1.isFinished(mockedCarOrder)).thenReturn(true);
        when(mockedTask2.isFinished(mockedCarOrder)).thenReturn(true);
        when(mockedTask3.isFinished(mockedCarOrder)).thenReturn(false);

        assertEquals(List.of(mockedTask1, mockedTask2), station.getFinishedTasks());
    }

    @Test
    void getFinishedTasks_whenWorkStationHasNoCurrentOrder_returnsEmptyList() {
        station = new WorkStation("TestWorkStation", List.of(mockedTask1, mockedTask2, mockedTask3));
        when(mockedTask1.isFinished(mockedCarOrder)).thenReturn(true);
        when(mockedTask2.isFinished(mockedCarOrder)).thenReturn(true);
        when(mockedTask3.isFinished(mockedCarOrder)).thenReturn(false);

        assertEquals(List.of(), station.getPendingTasks());
    }

    @Test
    void hasCompleted_TasksFinished() {
        station = new WorkStation("TestWorkStation", List.of(mockedTask1, mockedTask2));

        when(mockedTask1.isFinished(mockedCarOrder)).thenReturn(true);
        when(mockedTask2.isFinished(mockedCarOrder)).thenReturn(true);

        assertNull(station.getCarOrder());
        assertTrue(station.hasCompleted());
    }

    @Test
    void hasCompleted_OrderFinished() {
        station = new WorkStation("TestWorkStation", List.of(mockedTask1, mockedTask2));

        // mock tasks as not done
        when(mockedTask1.isFinished(mockedCarOrder)).thenReturn(false);
        when(mockedTask2.isFinished(mockedCarOrder)).thenReturn(false);

        // Because we haven't set a currentOrder for this workstation, it succeeds
        assertTrue(station.hasCompleted());
    }

/*    @Test
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
    }*/

    @Test
    void finishTask_finishesTask_and_updatesListeners() {

        when(mockedTask1.isFinished(mockedCarOrder)).thenReturn(true);
        when(mockedTask2.isFinished(mockedCarOrder)).thenReturn(true);
        when(mockedTask3.isFinished(mockedCarOrder)).thenReturn(false);

        var spoiler = new OptionCategory("Spoiler");
        var task1 = new AssemblyTask("TestTask", List.of("TestAction"), spoiler);
        var task2 = new AssemblyTask("TestTask", List.of("TestAction"), spoiler);

        int timeSpentOnTask1 = 45;
        int timeSpentOnTask2 = 35;

        station = new WorkStation("TestStation", List.of(mockedTask1, mockedTask2, task1, task2));
        station.updateCurrentOrder(mockedCarOrder);

        WorkStationListener wsListener = timeSpent -> assertEquals(timeSpentOnTask1,timeSpent);
        station.addListener(wsListener);

        var testSelections = new HashMap<OptionCategory, Option>();
        var spoilercategory = new OptionCategory("Spoiler");
        testSelections.put(spoilercategory, new Option(spoilercategory, "Low"));

        when(mockedCarOrder.getSelections()).thenReturn(testSelections);
        assertFalse(task1.isFinished(mockedCarOrder));
        assertFalse(task2.isFinished(mockedCarOrder));

        // given task gets finished
        station.finishTask(task1,timeSpentOnTask1);
        assertTrue(task1.isFinished(mockedCarOrder));

        // accumulated time for tasks gets calculated

        // remove listener1 because its assert isn't correct anymore
        station.removeListener(wsListener);
        WorkStationListener wsListener2 = timeSpent -> assertEquals(timeSpentOnTask1 + timeSpentOnTask2,timeSpent);
        station.addListener(wsListener2);

        station.finishTask(task2, timeSpentOnTask2);
        // every listener from this workstation gets updated with given accumulated time

    }

}