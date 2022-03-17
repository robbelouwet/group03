package domain.assembly;

import lombok.Getter;

import java.util.List;

public class AssemblyTask {
    @Getter
    private boolean finished;
    @Getter
    private String name;
    @Getter
    private List<String> actions;

    public AssemblyTask(String name, List<String> actions) {
        this.name = name;
        this.finished = false;
        this.actions = actions;
    }

    public void finishTask() {
        finished = true;
    }

    @Override
    public String toString() {
        return String.format("Task [%s]: is %s", name, finished ? "finished" : "pending");
    }

    public String getTaskInformation() {
        return this.toString();
    }
}
