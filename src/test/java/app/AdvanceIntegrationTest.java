package app;

import app.controllers.ManagerController;
import app.ui.interfaces.IManagerView;
import app.utils.ConsoleReader;
import domain.assembly.AssemblyTask;
import domain.order.CarOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.PersistenceFactory;
import services.ManagerFactory;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class AdvanceIntegrationTest {

    public AdvanceIntegrationTest() {
        ConsoleReader mReader = mock(ConsoleReader.class);
        ConsoleReader.setInstance(mReader);
    }

    /**
     * This method 'resets the state of our domain'
     */
    @BeforeEach
    public void reset() {
        // First re-initialize the persistence factory, THEN the manager factory
        PersistenceFactory.setInstance(new PersistenceFactory());
        ManagerFactory.setInstance(new ManagerFactory());
    }

    @BeforeEach
    public void setup() {
        // create 3 car orders from 1 model
        var model = PersistenceFactory.getInstance().getCarRepository().getModels().get(0);
        for (int i = 0; i < 3; i++) {
            ManagerFactory.getInstance().getCarOrderManager().selectModel(model);
            ManagerFactory.getInstance().getCarOrderManager().submitCarOrder(new HashMap<>() {{
                put("Body", "break");
                put("Color", "white");
                put("Engine", "performance");
                put("Gearbox", "5 speed automatic");
                put("Seats", "vinyl grey");
                put("Airco", "automatic");
                put("Wheels", "sports");

            }});
        }
    }

    @Test
    public void mainScenarioTest() {
        // verify we have 3 pending orders
        var allPending = PersistenceFactory.getInstance().getCarOrderCatalog().getOrders().stream().noneMatch(CarOrder::isFinished);
        var size = PersistenceFactory.getInstance().getCarOrderCatalog().getOrders().size();
        assertEquals(3, size);
        assertTrue(allPending);

        // now mock an IManagerView
        IManagerView mgrView = new IManagerView() {
            @Override
            public void confirmMove(int timeSpent) {
                (new ManagerController(this)).advanceAssemblyLine(timeSpent);
            }

            @Override
            public void showOverview(List<String> pendingOrders, List<String> simFinishedOrders, List<List<String>> pendingTasks, List<List<String>> finishedTasks) {
            }

            @Override
            public void showErrorMessage(String err) {
                throw new RuntimeException("Assembly line is blocked!");
            }
        };

        // now 'advance & clear' the assembly line 6 times
        // pre:     3 pending orders | empty assembly line | 0 finished orders
        // post:    0 pending orders | empty assembly line | 3 finished orders
        for (int i = 0; i < 6; i++) {
            mgrView.confirmMove(60);

            // TODO: call the CarMechanicView instead of calling the AssemblyLine directly, this is not end-to-end otherwise!
            var ws = ManagerFactory.getInstance().getAssemblyLineManager().getAssemblyLine().getWorkStations();
            ws.forEach(w -> w.getTasks().forEach(AssemblyTask::finishTask));
        }

        // now verify we have 3 finished orders
        var allFinished = PersistenceFactory.getInstance().getCarOrderCatalog().getOrders().stream().allMatch(CarOrder::isFinished);
        assertTrue(allFinished);

        // and 0 pending orders
        var sizePending = PersistenceFactory.getInstance().getCarOrderCatalog().getOrders().size();
        assertEquals(3, sizePending);
    }

    @Test
    public void alternateFlowTest() {
        // advance once
        ManagerFactory.getInstance().getAssemblyLineManager().advance(60);

        IManagerView mgrView = new IManagerView() {
            @Override
            public void confirmMove(int timeSpent) {
                (new ManagerController(this)).advanceAssemblyLine(timeSpent);
            }

            @Override
            public void showOverview(List<String> pendingOrders, List<String> simFinishedOrders, List<List<String>> pendingTasks, List<List<String>> finishedTasks) {
            }

            @Override
            public void showErrorMessage(String err) {
                throw new RuntimeException("Assembly line is blocked!");
            }
        };


        // trigger and catch an error message in the view, because the assembly line is blocked at that first workstation
        Throwable e = assertThrows(RuntimeException.class, () -> mgrView.confirmMove(60));
        assertEquals("Assembly line is blocked!", e.getMessage());
    }

}
