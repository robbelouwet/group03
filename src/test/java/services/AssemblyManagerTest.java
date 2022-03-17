package services;

import domain.assembly.AssemblyLine;
import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AssemblyManagerTest {
    private AssemblyManager assemblyManager;

    @BeforeEach
    public void setup() {
        // mock an AssemblyManager
        var aline = mock(AssemblyLine.class);
        when(aline.advance(anyInt())).thenReturn(true);

        // mock 2 tasks, with one finished
        var task1 = new AssemblyTask("test1", List.of("Test action"));
        task1.finishTask();
        var task2 = new AssemblyTask("test1", List.of("Test action"));
        task2.finishTask();
        var testTasks = List.of(task1, task2, new AssemblyTask("test2", List.of("Test action")));

        // create workstation with above tasks
        var ws1 = mock(WorkStation.class);
        when(ws1.getPendingTasks()).thenReturn(new ArrayList<>(testTasks));
        when(ws1.getName()).thenReturn("WS1");
        var ws2 = mock(WorkStation.class);
        when(ws2.getPendingTasks()).thenReturn(new ArrayList<>(testTasks));
        when(ws2.getName()).thenReturn("WS2");

        // Create an AssemblyManager with mocked AssemblyLine
        assemblyManager = new AssemblyManager(aline);
        when(aline.advance(anyInt())).thenReturn(true);

        when(assemblyManager.getAssemblyLine().getWorkStations()).thenReturn(new LinkedList<>(List.of(ws1, ws2)));
    }

    @Test
    void advance() {
        assertTrue(assemblyManager.advance(60));
    }

    @Test
    void getPendingTasks() {
        // the flatMap squishes the 2-dimensional list of tasks into 1
        var pendingTasks = assemblyManager.getPendingTasks().values()
                .stream().flatMap(Collection::stream).toList();

        assertEquals(2, pendingTasks.size());
        assertTrue(pendingTasks.stream().noneMatch(AssemblyTask::isFinished));
    }

    @Test
    void getFinishedTasks() {
        // the flatMap squishes the 2-dimensional list of tasks into 1
        var finishedTasks = assemblyManager.getFinishedTasks().values()
                .stream().flatMap(Collection::stream).toList();

        assertEquals(4, finishedTasks. size());
        assertTrue(finishedTasks.stream().allMatch(AssemblyTask::isFinished));
    }
}