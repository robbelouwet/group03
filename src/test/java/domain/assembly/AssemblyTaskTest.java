package domain.assembly;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AssemblyTaskTest {

    @Test
    void finishTask() {
        var task = new AssemblyTask("TestTask");
        task.finishTask();
        assertTrue(task.isFinished());
    }
}