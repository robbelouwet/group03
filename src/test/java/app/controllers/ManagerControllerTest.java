package app.controllers;

import app.ui.interfaces.IManagerView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.AssemblyManager;
import services.ManagerStore;

import java.util.List;

import static org.mockito.Mockito.*;

public class ManagerControllerTest {
    private ManagerController mgrController;
    private IManagerView mgrView;

    @BeforeEach
    public void setup() {
         // change this to false to see the exception being thrown
        var mockedStore = mock(ManagerStore.class);
        ManagerStore.setInstance(mockedStore);
        var mockedAManager = mock(AssemblyManager.class);
        when(mockedAManager.advance(anyInt())).thenReturn(true);
        when(mockedStore.getAssemblyLineManager()).thenReturn(mockedAManager);

        mgrView = new IManagerView() {
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

        mgrController = new ManagerController(mgrView);

    }

    @Test
    void advanceAssemblyLine() {
        // if something goes wrong, the controller throws an exception,
        // so just executing and expecting no exception is sufficient
        mgrController.advanceAssemblyLine(60);
    }
}