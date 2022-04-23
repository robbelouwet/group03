package app;

import app.controllers.ControllerStore;
import app.ui.interfaces.IManagerView;
import app.utils.ConsoleReader;
import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;
import domain.order.CarOrder;
import domain.scheduler.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.CarCatalog;
import persistence.CarOrderRepository;
import services.ManagerStore;
import utils.TestObjects;

import java.util.*;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class AdvanceIntegrationTest {

    public AdvanceIntegrationTest() {
        ConsoleReader mReader = mock(ConsoleReader.class);
        ConsoleReader.setInstance(mReader);
    }

    private ManagerStore managerStore;
    private ControllerStore controllerStore;

    @BeforeEach
    public void setup() {
        // create 3 car orders from 1 model
        var model = (new CarCatalog()).getModels().get(0);
        var orders = new ArrayList<CarOrder>();
        for (int i = 0; i < 3; i++) {
            orders.add(TestObjects.getCarOrder());
        }

        managerStore = new ManagerStore(new CarOrderRepository(orders));
        controllerStore = new ControllerStore(managerStore);
    }

    @Test
    public void mainScenarioTest() {
        // verify we have 3 pending orders
        var allPending = managerStore.getCarOrderManager().getPendingOrders();
        var sizePending = allPending.size();
        assertEquals(3, sizePending);
        assertTrue(allPending.stream().noneMatch(CarOrder::isFinished));

        // and 0 finished orders
        var allFinished = managerStore.getCarOrderManager().getFinishedOrders();
        var sizeFinished = allFinished.size();
        assertEquals(0, sizeFinished);
        assertTrue(allFinished.stream().allMatch(CarOrder::isFinished));

        var controller = controllerStore.getManagerController();
        // now mock an IManagerView
        IManagerView mgrView = new IManagerView() {
            @Override
            public void confirmMove(int timeSpent) {
                controller.advanceAssemblyLine(timeSpent);
            }

            @Override
            public void showAdvanceOverview() {
                String action = ConsoleReader.getInstance().ask("Advance Assembly Line? [yes] | [no]");
                while (!(action.equals("yes") || action.equals("no"))) {
                    ConsoleReader.getInstance().println("This is not a valid option.");
                    action = ConsoleReader.getInstance().ask("Advance Assembly Line? [yes] | [no]");
                }
                if (action.equals("yes")) {
                    int time = new Random().nextInt(100);
                    confirmMove(time);
                }
            }

            @Override
            public void showErrorMessage(String err) {
                throw new RuntimeException("Assembly line is blocked!");
            }
        };
        controller.setUi(mgrView);

        // now 'advance & clear' the assembly line 6 times
        // pre:     3 pending orders | empty assembly line | 0 finished orders
        // post:    0 pending orders | empty assembly line | 3 finished orders
        var man = managerStore.getMechanicManager();

        var func = new Consumer<WorkStation>() {
            @Override
            public void accept(WorkStation workStation) {
                var func = new Consumer<AssemblyTask>() {

                    @Override
                    public void accept(AssemblyTask s) {
                        man.selectTask(s.getName());
                        man.finishTask();
                    }
                };
                man.selectWorkStation(workStation.getName());
                workStation.getPendingTasks().forEach(func);
            }
        };
        for (int i = 0; i < 6; i++) {
            managerStore.getAssemblyLineManager().advance(60);

            var ws = managerStore.getAssemblyLineManager().getBusyWorkStations();
            ws.forEach(func);
        }

        // verify we have 0 pending orders
        var allPendingAfter = managerStore.getCarOrderManager().getPendingOrders();
        var sizePendingAfter = allPendingAfter.size();
        assertEquals(0, sizePendingAfter);
        assertTrue(allPendingAfter.stream().noneMatch(CarOrder::isFinished));

        // and 3 finished orders
        var allFinishedAfter = managerStore.getCarOrderManager().getFinishedOrders();
        var sizeFinishedAfter = allFinishedAfter.size();
        assertEquals(3, sizeFinishedAfter);
        assertTrue(allFinishedAfter.stream().allMatch(CarOrder::isFinished));
    }

    @Test
    public void alternateFlowTest() {
        // advance once
        managerStore.getAssemblyLineManager().advance(60);

        var view = new IManagerView() {
            @Override
            public void confirmMove(int timeSpent) {}

            @Override
            public void showAdvanceOverview() {
                String action = ConsoleReader.getInstance().ask("Advance Assembly Line? [yes] | [no]");
                while (!(action.equals("yes") || action.equals("no"))) {
                    ConsoleReader.getInstance().println("This is not a valid option.");
                    action = ConsoleReader.getInstance().ask("Advance Assembly Line? [yes] | [no]");
                }
                if (action.equals("yes")) {
                    int time = new Random().nextInt(100);
                    confirmMove(time);
                }
            }

            @Override
            public void showErrorMessage(String err) {
                throw new RuntimeException("Assembly line is blocked!");
            }
        };

        var controller = controllerStore.getManagerController();
        controller.setUi(view);
        // trigger and catch an error message in the view, because the assembly line is blocked at that first workstation
        Throwable e = assertThrows(RuntimeException.class, () -> controller.advanceAssemblyLine(60));

        assertEquals("Assembly line is blocked!", e.getMessage());
    }

}
