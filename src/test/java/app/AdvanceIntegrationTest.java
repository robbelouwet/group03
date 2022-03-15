package app;

import app.controllers.ManagerController;
import app.ui.AppTextView;
import app.ui.interfaces.IManagerView;
import app.utils.ConsoleReader;
import app.ui.interfaces.IAppView;
import domain.car.CarModel;
import domain.order.CarOrder;
import org.junit.jupiter.api.Test;
import services.ManagerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

public class AdvanceIntegrationTest {
    private IAppView appView;
    private final ConsoleReader mockedReader;

    public AdvanceIntegrationTest() {
        appView = new AppTextView();

        ConsoleReader mReader = mock(ConsoleReader.class);
        ConsoleReader.setInstance(mReader);
        mockedReader = mReader;
    }

    @Test
    public void mainScenarioTest() {
        // setup preconditions
        // e.g.: create and place orders on assembly line
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

        when(mockedReader.ask("Who are you? [manager] | [garage holder] | [mechanic] | [quit]")).thenReturn("manager");
        when(mockedReader.ask("Advance Assembly Line? [yes] | [no]")).thenReturn("yes");
        when(mockedReader.ask("Time spent? [positive number]")).thenReturn("20");
        when(mockedReader.ask("Who are you? [manager] | [garage holder] | [mechanic] | [quit]")).thenReturn("quit");

        // TODO

        appView.start();
    }

    @Test
    public void alternateFlowTest() {
        // first we need to create a CarModel and select options for it, before the assembly line can start working on it
        var model = ManagerFactory.getInstance().getCarOrderManager().getCarRepository().getModels().get(0);
        ManagerFactory.getInstance().getCarOrderManager().selectModel(model);

        // create 3 CarOrders
        var orders = new ArrayList<CarOrder>();
        for (int i = 0; i < 3; i++) {
            orders.add(ManagerFactory.getInstance().getCarOrderManager().submitCarOrder(new HashMap<>() {{
                put("Body", "break");
                put("Color", "white");
                put("Engine", "performance");
                put("Gearbox", "5 speed automatic");
                put("Seats", "vinyl grey");
                put("Airco", "automatic");
                put("Wheels", "sports");

            }}));
        }

        // advance the empty assembly line once, to put the newly created order on the first work station
        ManagerFactory.getInstance().getAssemblyLineManager().advance(60);
        ManagerFactory.getInstance().getAssemblyLineManager().

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
