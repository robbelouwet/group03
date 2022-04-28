package domain.assembly;

import domain.scheduler.ProductionScheduler;
import domain.scheduler.TimeManager;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.LinkedList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AssemblyLineTest {


    @BeforeEach
    public void setup() {
        // We mock the workstations because we ONLY want to test AssemblyLine's behaviour
        var fakeStation = mock(WorkStation.class);
        when(fakeStation.hasCompleted()).thenReturn(true);

        AssemblyLine assemblyLine = new AssemblyLine(
                new LinkedList<>(Arrays.asList(fakeStation, fakeStation, fakeStation)), mock(ProductionScheduler.class), new TimeManager());
    }

}