package app;

import app.controllers.ManagerController;
import app.ui.interfaces.IManagerView;
import app.utils.ConsoleReader;
import domain.assembly.AssemblyTask;
import domain.order.CarOrder;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class AdvanceIntegrationTest {

    public AdvanceIntegrationTest() {
        ConsoleReader mReader = mock(ConsoleReader.class);
        ConsoleReader.setInstance(mReader);
    }

    @BeforeEach
    public void setup() {
        // create 3 car orders from 1 model
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

        ManagerStore.getInstance().init(new CarOrderRepository(orders));
    }

    @Test
    public void mainScenarioTest() {
        // verify we have 3 pending orders
        var allPending = ManagerStore.getInstance().getCarOrderManager().getPendingOrders();
        var sizePending = allPending.size();
        assertEquals(3, sizePending);
        assertTrue(allPending.stream().noneMatch(CarOrder::isFinished));

        // and 0 finished orders
        var allFinished = ManagerStore.getInstance().getCarOrderManager().getFinishedOrders();
        var sizeFinished = allFinished.size();
        assertEquals(0, sizeFinished);
        assertTrue(allFinished.stream().allMatch(CarOrder::isFinished));

        // now mock an IManagerView
        IManagerView mgrView = new IManagerView() {
            @Override
            public void confirmMove(int timeSpent) {
                (new ManagerController(this)).advanceAssemblyLine(timeSpent);
            }

            @Override
            public void showOverview(List<String> pendingOrders,
                                     List<String> simFinishedOrders,
                                     Map<String, List<String>> pendingTasks,
                                     Map<String, List<String>> finishedTasks) {

            }

            @Override
            public void showErrorMessage(String err) {
                throw new RuntimeException("Assembly line is blocked!");
            }

            @Override
            public void showAssemblyLineStatusAfterMove(List<String> pendingOrders) {

            }
        };

        // now 'advance & clear' the assembly line 6 times
        // pre:     3 pending orders | empty assembly line | 0 finished orders
        // post:    0 pending orders | empty assembly line | 3 finished orders
        for (int i = 0; i < 6; i++) {
            mgrView.confirmMove(60);

            // TODO: call the CarMechanicView instead of calling the AssemblyLine directly, this is not end-to-end otherwise!
            var ws = ManagerStore.getInstance().getAssemblyLineManager().getAssemblyLine().getWorkStations();
            ws.forEach(w -> w.getPendingTasks().forEach(AssemblyTask::finishTask));
        }

        // verify we have 0 pending orders
        var allPendingAfter = ManagerStore.getInstance().getCarOrderManager().getPendingOrders();
        var sizePendingAfter = allPendingAfter.size();
        assertEquals(0, sizePendingAfter);
        assertTrue(allPendingAfter.stream().noneMatch(CarOrder::isFinished));

        // and 3 finished orders
        var allFinishedAfter = ManagerStore.getInstance().getCarOrderManager().getFinishedOrders();
        var sizeFinishedAfter = allFinishedAfter.size();
        assertEquals(3, sizeFinishedAfter);
        assertTrue(allFinishedAfter.stream().allMatch(CarOrder::isFinished));
    }

    @Test
    public void alternateFlowTest() {
        // advance once
        ManagerStore.getInstance().getAssemblyLineManager().advance(60);

        IManagerView mgrView = new IManagerView() {
            @Override
            public void confirmMove(int timeSpent) {
                (new ManagerController(this)).advanceAssemblyLine(timeSpent);
            }

            @Override
            public void showOverview(List<String> pendingOrders,
                                     List<String> simFinishedOrders,
                                     Map<String, List<String>> pendingTasks,
                                     Map<String, List<String>> finishedTasks) {

            }

            @Override
            public void showErrorMessage(String err) {
                throw new RuntimeException("Assembly line is blocked!");
            }

            @Override
            public void showAssemblyLineStatusAfterMove(List<String> pendingOrders) {

            }
        };


        // trigger and catch an error message in the view, because the assembly line is blocked at that first workstation
        Throwable e = assertThrows(RuntimeException.class, () -> mgrView.confirmMove(60));
        assertEquals("Assembly line is blocked!", e.getMessage());
    }

}
