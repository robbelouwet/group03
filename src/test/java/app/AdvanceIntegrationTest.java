package app;

import app.controllers.ManagerController;
import app.ui.interfaces.IManagerView;
import app.utils.ConsoleReader;
import domain.assembly.AssemblyTask;
import domain.order.CarOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.CarOrderCatalog;
import persistence.CarRepository;
import persistence.PersistenceFactory;
import services.AssemblyManager;
import services.CarOrderManager;
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

    @BeforeEach
    public void reset() {
        ManagerFactory.setInstance(new ManagerFactory());
        PersistenceFactory.setInstance(new PersistenceFactory());
    }

    @Test
    public void mainScenarioTest() {
        // setup preconditions
        // e.g.: create and place orders on assembly line
        // first we need to create a CarModel and select options for it, before the assembly line can start working on it
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

        var allPending = PersistenceFactory.getInstance().getCarOrderCatalog().getOrders().stream().noneMatch(CarOrder::isFinished);
        assertTrue(allPending);

        // now mock a manager view for testing to catch the exception later on
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

        // now advance 6 times
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

        var size = PersistenceFactory.getInstance().getCarOrderCatalog().getOrders().size();
        assertEquals(3, size);
    }

    @Test
    public void alternateFlowTest() {
        // first we need to create a CarModel and select options for it, before the assembly line can start working on it
        var model = ManagerFactory.getInstance().getCarOrderManager().getCarRepository().getModels().get(0);
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

        // advance the empty assembly line once, to put the newly created order on the first work station
        ManagerFactory.getInstance().getAssemblyLineManager().advance(60);

        // now mock a manager view for testing to catch the exception later on
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

        // when triggering the mocked view to confirm the move and advance the assembly line, the controller will call the showErrorMessage method
        // because the assembly line is blocked. The controller then triggers an exception in our mocked view, which we expect
        // catch the exception
        Throwable e = assertThrows(RuntimeException.class, () -> mgrView.confirmMove(60));
        assertEquals("Assembly line is blocked!", e.getMessage());
    }

}
