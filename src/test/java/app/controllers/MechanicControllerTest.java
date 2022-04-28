package app.controllers;

import app.ui.interfaces.ICarMechanicView;
import domain.assembly.AssemblyLine;
import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;
import domain.order.CarOrder;
import domain.scheduler.DateTime;
import domain.scheduler.TimeManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.CarCatalog;
import services.AssemblyManager;
import services.ManagerStore;
import services.MechanicManager;
import utils.TestObjects;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


public class MechanicControllerTest {
    ManagerStore managerStore;
    ControllerStore controllerStore;
    /*

    @BeforeEach
    public void setup() {
        managerStore = mock(ManagerStore.class);

        var task1 = new AssemblyTask("task1", List.of("action1.1", "action1.2"));
        var task2 = new AssemblyTask("task2", List.of("action2.1"));
        task2.finishTask();
        var tasksWS1 = List.of(task1, task2);

        var task3 = new AssemblyTask("task3", List.of("action3.1", "action3.2"));
        var task4 = new AssemblyTask("task4", List.of("action4.1"));
        task4.finishTask();
        var task5 = new AssemblyTask("task5", List.of("action5.1", "action5.2", "action5.3"));
        var tasksWS2 = List.of(task3, task4, task5);

        var mockedWorkStation1 = new WorkStation("mockedWorkStation1", tasksWS1);
        var mockedWorkStation2 = new WorkStation("mockedWorkStation2", tasksWS2);
        var model = (new CarCatalog()).getModels().get(0);

        var order = TestObjects.getCarOrder();
        mockedWorkStation1.updateCurrentOrder(order);
        mockedWorkStation2.updateCurrentOrder(order);
        var mockedAssemblyLine = new AssemblyLine(new LinkedList<>(List.of(mockedWorkStation1, mockedWorkStation2)), null, new TimeManager());

        var mockedMechanicManager = new MechanicManager(mockedAssemblyLine);
        when(managerStore.getMechanicManager()).thenReturn(mockedMechanicManager);
        var mockedAssemblyManager = new AssemblyManager(mockedAssemblyLine);
        when(managerStore.getAssemblyLineManager()).thenReturn(mockedAssemblyManager);

        controllerStore = new ControllerStore(managerStore);
    }

    @Test
    void showMainMenu() {
        var view = new ICarMechanicView() {

            @Override
            public void showWorkStations(List<String> availableWorkstations) {
                assertEquals(availableWorkstations.size(), 2);
                assertEquals(availableWorkstations.get(0), "mockedWorkStation1");
                assertEquals(availableWorkstations.get(1), "mockedWorkStation2");
            }

            @Override
            public void showAvailableTasks(List<String> workStationTasks) {


            }

            @Override
            public void showTaskInfo(String info, List<String> actions) {

            }
        };

        var controller = controllerStore.getMechanicController();
        controller.setUi(view);
        controller.showMainMenu();
    }

    @Test
    void selectWorkStation() {
        var view = new ICarMechanicView() {

            @Override
            public void showWorkStations(List<String> availableWorkstations) {
                assertEquals(availableWorkstations.size(), 2);
                assertEquals(availableWorkstations.get(0), "mockedWorkStation1");
                assertEquals(availableWorkstations.get(1), "mockedWorkStation2");
            }

            @Override
            public void showAvailableTasks(List<String> workStationTasks) {
                assertEquals(workStationTasks.size(), 2);
                assertEquals(workStationTasks.get(0), "Task [task3]: is pending");
                assertEquals(workStationTasks.get(1), "Task [task5]: is pending");
            }

            @Override
            public void showTaskInfo(String info, List<String> actions) {

            }
        };

        var controller = controllerStore.getMechanicController();
        controller.setUi(view);
        controller.selectWorkStation("mockedWorkStation2");
    }

    @Test
    void selectWorkStation_givenInvalidWorkStationName() {
        var view = new ICarMechanicView() {

            @Override
            public void showWorkStations(List<String> availableWorkstations) {
            }

            @Override
            public void showAvailableTasks(List<String> workStationTasks) {
            }

            @Override
            public void showTaskInfo(String info, List<String> actions) {

            }
        };

        var controller = controllerStore.getMechanicController();
        controller.setUi(view);
        assertThrows(NoSuchElementException.class, () -> controller.selectWorkStation("invalidWorkStationName"));
    }

    @Test
    void selectTask() {
        var controller = controllerStore.getMechanicController();

        var view = new ICarMechanicView() {
            @Override
            public void showWorkStations(List<String> availableWorkstations) {
            }

            @Override
            public void showAvailableTasks(List<String> workStationTasks) {
                controller.selectTask("task5");
            }

            @Override
            public void showTaskInfo(String info, List<String> actions) {
                assertEquals(info, "Task [task5]: is pending");
                assertEquals(actions, List.of("action5.1", "action5.2", "action5.3"));
            }
        };

        controller.setUi(view);
        controller.selectWorkStation("mockedWorkStation2");
    }

    @Test
    void selectTask_givenInvalidTaskName() {
        var controller = controllerStore.getMechanicController();

        var view = new ICarMechanicView() {
            @Override
            public void showWorkStations(List<String> availableWorkstations) {
            }

            @Override
            public void showAvailableTasks(List<String> workStationTasks) {
                assertThrows(NoSuchElementException.class, () -> controller.selectTask("invalidTaskName"));
            }

            @Override
            public void showTaskInfo(String info, List<String> actions) {
                assertEquals(info, "Task [task5]: is pending");
                assertEquals(actions, List.of("action5.1", "action5.2", "action5.3"));
            }
        };

        controller.setUi(view);
        controller.selectWorkStation("mockedWorkStation2");
    }

    @Test
    void selectTask_givenFinishedTaskName() {
        var controller = controllerStore.getMechanicController();

        var view = new ICarMechanicView() {
            @Override
            public void showWorkStations(List<String> availableWorkstations) {
            }

            @Override
            public void showAvailableTasks(List<String> workStationTasks) {
                assertThrows(NoSuchElementException.class, () -> controller.selectTask("task4"));
            }

            @Override
            public void showTaskInfo(String info, List<String> actions) {
                assertEquals(info, "Task [task5]: is pending");
                assertEquals(actions, List.of("action5.1", "action5.2", "action5.3"));
            }
        };

        controller.setUi(view);
        System.out.println(managerStore.getAssemblyLineManager().getBusyWorkStations().size());
        controller.selectWorkStation("mockedWorkStation2");
    }

    @Test
    void finishTask() {
        var controller = controllerStore.getMechanicController();

        var view = new ICarMechanicView() {
            private boolean taskFinished = false;

            @Override
            public void showWorkStations(List<String> availableWorkstations) {
            }

            @Override
            public void showAvailableTasks(List<String> workStationTasks) {
                if (!taskFinished) {
                    assertEquals(workStationTasks.size(), 2);
                    assertEquals(workStationTasks.get(0), "Task [task3]: is pending");
                    assertEquals(workStationTasks.get(1), "Task [task5]: is pending");
                } else {
                    assertEquals(workStationTasks.size(), 1);
                    assertEquals(workStationTasks.get(0), "Task [task5]: is pending");
                }
            }

            @Override
            public void showTaskInfo(String info, List<String> actions) {
                taskFinished = true;
                controller.finishTask();
            }
        };

        controller.setUi(view);
        controller.selectWorkStation("mockedWorkStation2");
        controller.selectTask("task3");
    }

     */
}
