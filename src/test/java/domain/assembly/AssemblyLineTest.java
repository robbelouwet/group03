package domain.assembly;

import domain.scheduler.ProductionScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AssemblyLineTest {
    private AssemblyLine assemblyLine;

    @BeforeEach
    public void setup() {
        // We mock the workstations because we ONLY want to test AssemblyLine's behaviour
        var fakeStation = mock(WorkStation.class);
        when(fakeStation.hasCompleted()).thenReturn(true);

        assemblyLine = new AssemblyLine(
                new LinkedList<>(Arrays.asList(fakeStation, fakeStation, fakeStation)), mock(ProductionScheduler.class));
    }

    @Test
    void advance() {
        assertTrue(assemblyLine.advance(60, false));
    }
}