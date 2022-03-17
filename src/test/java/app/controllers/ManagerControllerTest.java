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
        // first mock the ManagerFactory and its AssemblyLineManager
        // in order to mock AssemblyLineManager's advance method and isolate the controller's behaviour
        var mockedFactory = mock(ManagerStore.class);
        var mockedAssemblyManager = mock(AssemblyManager.class);
        when(mockedFactory.getAssemblyLineManager()).thenReturn(mockedAssemblyManager);
        when(mockedAssemblyManager.advance(anyInt())).thenReturn(true); // change this to false to see the exception being thrown

        // re-initialize the persistence factory, THEN the manager factory
        PersistenceFactory.setInstance(new PersistenceFactory());
        ManagerStore.setInstance(mockedFactory);

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