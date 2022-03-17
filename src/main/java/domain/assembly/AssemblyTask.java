package domain.assembly;

import lombok.Getter;

/**
 * Class {@code AssemblyTask} is the representation of a
 * task that needs to be performed on a {@code CarOrder} on the {@code AssemblyLine}.
 */
public class AssemblyTask {
    @Getter private boolean finished;
    @Getter private String name;

    /**
     * @param name The name of the {@code AssemblyTask} that needs to be performed at a {@code WorkStation}
     */
    public AssemblyTask(String name) {
        this.name = name;
        this.finished = false;
    }

    public void finishTask(){
        finished = true;
    }

    @Override
    public String toString() {
        return String.format("Task %s: is %s", name, finished ? "finished" : "pending");
    }
}
