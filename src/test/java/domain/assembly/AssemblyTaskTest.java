package domain.assembly;

import domain.car.options.OptionCategory;
import org.junit.jupiter.api.Test;
import utils.TestObjects;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AssemblyTaskTest {
    @Test
    void finishTask_onAnUnfinishedTask_withAnOptionThatIsNotNone() {
        var order = TestObjects.getCarOrder();
        // this results in a car order for following model
        // Model A
        // body: Sedan
        // ...

        var seats = new OptionCategory("Seats");
        var task = new AssemblyTask("TestTask", List.of("TestAction"), seats);
        assertFalse(task.isFinished(order));
        task.finishTask(50);
        assertTrue(task.isFinished(order));
    }

    @Test
    void finishTask_onAnUnfinishedTask_withAnOptionThatIsNone() {
        var order = TestObjects.getCarOrder();
        // this results in a car order for following model
        // Model A
        // spoiler: None
        // ...

        var spoiler = new OptionCategory("Spoiler");
        var task = new AssemblyTask("TestTask", List.of("TestAction"), spoiler);

        // task should already be finished since there is nothing
        // left to be done when the option is None
        assertTrue(task.isFinished(order));
        task.finishTask(50);
        assertTrue(task.isFinished(order));
    }

}