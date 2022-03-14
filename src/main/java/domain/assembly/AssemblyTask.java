package domain.assembly;

import lombok.Getter;

public class AssemblyTask {
    @Getter private boolean finished;
    @Getter private String name;

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
