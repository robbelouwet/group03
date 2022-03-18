package app.controllers;

import app.ui.interfaces.IManagerView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.AssemblyManager;
import services.CarOrderManager;
import services.ManagerStore;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class ManagerControllerTest {
    private ManagerController mgrController;
    private IManagerView mgrView;

    @BeforeEach
    public void setup() {
        // change this to false to see the exception being thrown
        var mockedStore = mock(ManagerStore.class);
        var mockedAManager = mock(AssemblyManager.class);
        when(mockedAManager.advance(anyInt())).thenReturn(true);
        var mockedCOManager = mock(CarOrderManager.class);
        when(mockedCOManager.getCarModels()).thenReturn(new LinkedList<>());
        when(mockedStore.getAssemblyLineManager()).thenReturn(mockedAManager);
        when(mockedStore.getCarOrderManager()).thenReturn(mockedCOManager);

        var controllerStore = new ControllerStore(mockedStore);

        mgrView = new IManagerView() {
            @Override
            public void confirmMove(int timeSpent) {
                controllerStore.getManagerController().advanceAssemblyLine(timeSpent);
            }

            @Override
            public void showOverview(Map<String, String> pendingOrders,
                                     Map<String, String> simFinishedOrders,
                                     Map<String, List<String>> pendingTasks,
                                     Map<String, List<String>> finishedTasks) {

            }

            @Override
            public void showErrorMessage(String err) {
                throw new RuntimeException("Assembly line is blocked!");
            }

            @Override
            public void showAssemblyLineStatusAfterMove(Map<String, String> pendingOrders) {

            }
        };

        mgrController = controllerStore.getManagerController();
        mgrController.setUi(mgrView);
    }

    @Test
    void advanceAssemblyLine() {
        // if something goes wrong, the controller throws an exception,
        // so just executing and expecting no exception is sufficient
        mgrController.advanceAssemblyLine(60);
    }
}