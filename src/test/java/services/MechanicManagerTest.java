package services;

import domain.assembly.AssemblyLine;
import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;
import domain.car.options.OptionCategory;
import domain.order.CarOrder;
import domain.scheduler.DateTime;
import domain.scheduler.FIFOSchedulingAlgorithm;
import domain.scheduler.ProductionScheduler;
import domain.scheduler.TimeManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.CarCatalog;
import persistence.CarOrderRepository;
import utils.TestObjects;

import java.util.*;
import domain.assembly.WorkStation;

import static org.junit.jupiter.api.Assertions.*;


public class MechanicManagerTest {
    private MechanicManager mechanicManager;
    private WorkStation workStation;

    @BeforeEach
    public void setup() {

        var wheels = new OptionCategory("Wheels");
        var airco = new OptionCategory("Airco");
        var seats = new OptionCategory("Seats");
        var gearbox = new OptionCategory("Gearbox");

        var task1 = new AssemblyTask("task1", List.of("action1.1", "action1.2"), airco);
        var task2 = new AssemblyTask("task2", List.of("action2.1"), wheels);
        task2.finishTask(30);
        var task3 = new AssemblyTask("task3", List.of("action3.1", "action3.2"), airco);
        var tasksWS1 = List.of(task1, task2, task3);

        var mockedWorkStation1 = new WorkStation("mockedWorkStation1", tasksWS1);
        workStation = mockedWorkStation1;
        var order = TestObjects.getCarOrder();
        workStation.updateCurrentOrder(order);

        var timeManager = new TimeManager();
        var repo = new CarOrderRepository();
        var scheduler = new ProductionScheduler(
                repo,
                timeManager,
                new FIFOSchedulingAlgorithm()
        );

        mechanicManager = new MechanicManager(new AssemblyLine(new LinkedList<>(List.of(mockedWorkStation1)), scheduler, new TimeManager()));
        mechanicManager.selectWorkStation(mockedWorkStation1.getName());
        mechanicManager.selectTask(task1.getName());

    }

    @Test
    void selectTask_givenValidTaskNameAndHasCurrentWorkStation() {
        AssemblyTask selectedTask = mechanicManager.selectTask("task1");
        assertEquals(selectedTask.getName(), "task1");
        assertEquals(selectedTask.getActions(), List.of("action1.1", "action1.2"));
    }

    @Test
    void selectTask_givenInValidTaskNameAndHasCurrentWorkStation() {
        assertThrows(NoSuchElementException.class, () -> mechanicManager.selectTask("invalidTaskName"));
    }

    @Test
    void selectTask_givenValidTaskNameAndHasNoCurrentWorkStation() {
        var wheels = new OptionCategory("Wheels");
        var airco = new OptionCategory("Airco");
        var seats = new OptionCategory("Seats");
        var gearbox = new OptionCategory("Gearbox");

        var task1 = new AssemblyTask("task1", List.of("action1.1", "action1.2"), airco);
        var task2 = new AssemblyTask("task2", List.of("action2.1"), wheels);
        task2.finishTask(30);
        var task3 = new AssemblyTask("task3", List.of("action3.1", "action3.2"), airco);
        var tasksWS1 = List.of(task1, task2, task3);

        var mockedWorkStation1 = new WorkStation("mockedWorkStation1", tasksWS1);

        var timeManager = new TimeManager();
        var repo = new CarOrderRepository();
        var scheduler = new ProductionScheduler(
                repo,
                timeManager,
                new FIFOSchedulingAlgorithm()
        );

        mechanicManager = new MechanicManager(new AssemblyLine(new LinkedList<>(List.of(mockedWorkStation1)), scheduler, new TimeManager()));
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> mechanicManager.selectTask("task1"));
        assertEquals(exception.getMessage(), "There is no current workstation selected.");
    }

    @Test
    void finishTask_whenSelectedTaskIsAlreadyFinished() {

        var order = TestObjects.getCarOrder();

        assertFalse(mechanicManager.getSelectedTask().isFinished(order));
        mechanicManager.finishTask(30);
        assertTrue(mechanicManager.getSelectedTask().isFinished(order));
    }

    @Test
    void finishTask_whenNoSelectedTask() {
        var timeManager = new TimeManager();
        var repo = new CarOrderRepository();
        var scheduler = new ProductionScheduler(
                repo,
                timeManager,
                new FIFOSchedulingAlgorithm()
        );

        mechanicManager = new MechanicManager(new AssemblyLine(new LinkedList<>(List.of(workStation)), scheduler, new TimeManager()));
        mechanicManager.selectWorkStation(workStation.getName());
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> mechanicManager.finishTask(30));
        assertEquals(exception.getMessage(), "There is no selected task.");
    }

    @Test
    void getTaskNames_returnsListWithNamesOfUnfinishedTasks() {
        List<String> taskNames = mechanicManager.getTaskNames();


        var wheels = new OptionCategory("Wheels");
        var airco = new OptionCategory("Airco");
        var seats = new OptionCategory("Seats");
        var gearbox = new OptionCategory("Gearbox");

        var task1 = new AssemblyTask("task1", List.of("action1.1", "action1.2"), airco);
        var task3 = new AssemblyTask("task3", List.of("action3.1", "action3.2"), airco);

        var order = TestObjects.getCarOrder();
        assertEquals(taskNames, List.of(task1.getInformation(order), task3.getInformation(order)));
    }

    @Test
    void getTaskNames_throwsIllegalStateException_whenNoCurrentWorkstation() {

        var wheels = new OptionCategory("Wheels");
        var airco = new OptionCategory("Airco");
        var seats = new OptionCategory("Seats");
        var gearbox = new OptionCategory("Gearbox");

        var task1 = new AssemblyTask("task1", List.of("action1.1", "action1.2"), airco);
        var task2 = new AssemblyTask("task2", List.of("action2.1"), wheels);
        task2.finishTask(30);
        var task3 = new AssemblyTask("task3", List.of("action3.1", "action3.2"), airco);
        var tasksWS1 = List.of(task1, task2, task3);

        var mockedWorkStation1 = new WorkStation("mockedWorkStation1", tasksWS1);

        var timeManager = new TimeManager();
        var repo = new CarOrderRepository();
        var scheduler = new ProductionScheduler(
                repo,
                timeManager,
                new FIFOSchedulingAlgorithm()
        );

        mechanicManager = new MechanicManager(new AssemblyLine(new LinkedList<>(List.of(mockedWorkStation1)), scheduler, new TimeManager()));
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> mechanicManager.getTaskNames());
        assertEquals(exception.getMessage(), "There is no current workstation selected.");
    }


    @Test
    void isValidTask() {
        assertTrue(mechanicManager.isValidTask("task1"));
        assertTrue(mechanicManager.isValidTask("task3"));

        // unfinished tasks are not validTasks to be returned
        assertFalse(mechanicManager.isValidTask("task2"));

        // invalid task name
        assertFalse(mechanicManager.isValidTask("invalidTaskname"));
    }

    @Test
    void isValidTask_throwsIllegalStateException_whenNoCurrentWorkstation() {

        var wheels = new OptionCategory("Wheels");
        var airco = new OptionCategory("Airco");
        var seats = new OptionCategory("Seats");
        var gearbox = new OptionCategory("Gearbox");

        var task1 = new AssemblyTask("task1", List.of("action1.1", "action1.2"), airco);
        var task2 = new AssemblyTask("task2", List.of("action2.1"), wheels);
        task2.finishTask(30);
        var task3 = new AssemblyTask("task3", List.of("action3.1", "action3.2"), airco);
        var tasksWS1 = List.of(task1, task2, task3);

        var mockedWorkStation1 = new WorkStation("mockedWorkStation1", tasksWS1);

        var timeManager = new TimeManager();
        var repo = new CarOrderRepository();
        var scheduler = new ProductionScheduler(
                repo,
                timeManager,
                new FIFOSchedulingAlgorithm()
        );

        mechanicManager = new MechanicManager(new AssemblyLine(new LinkedList<>(List.of(mockedWorkStation1)), scheduler,new TimeManager()));
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> mechanicManager.isValidTask("task1"));
        assertEquals(exception.getMessage(), "There is no current workstation selected.");
    }




}
