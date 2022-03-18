package services;

import domain.assembly.AssemblyLine;
import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;
import domain.order.CarOrder;
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
        when(aline.advance(anyInt(), anyBoolean())).thenReturn(true);

        // mock 2 tasks, with one finished
        var task1 = new AssemblyTask("test1", List.of("Test action"));
        task1.finishTask();
        var task2 = new AssemblyTask("test1", List.of("Test action"));
        task2.finishTask();
        var task3 = new AssemblyTask("test2", List.of("Test action"));
        var testTasks = List.of(task1, task2, task3);

        // create workstation with above tasks
        var ws1 = mock(WorkStation.class);
        when(ws1.getPendingTasks()).thenReturn(List.of());
        when(ws1.getName()).thenReturn("WS1");
        var ws2 = mock(WorkStation.class);
        when(ws2.getPendingTasks()).thenReturn(List.of(task3));
        when(ws2.getName()).thenReturn("WS2");

        // Create an AssemblyManager with mocked AssemblyLine
        assemblyManager = new AssemblyManager(aline);
        when(aline.advance(anyInt(), anyBoolean())).thenReturn(true);
        when(aline.getWorkStations()).thenReturn(new LinkedList<>(List.of(ws1, ws2)));

        // when(assemblyManager.getAssemblyLine().getWorkStations()).thenReturn(new LinkedList<>(List.of(ws1, ws2)));
    }

    @Test
    void advance() {
        assertTrue(assemblyManager.advance(60));
    }
}