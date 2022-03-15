package domain.assembly;

import lombok.Getter;

import java.util.List;

public class AssemblyTask {
    @Getter private boolean finished;
    @Getter private String name;

    public AssemblyTask(String name) {
        this.name = name;
        this.finished = false;
    }

    public static boolean isTaskName(String name, List<AssemblyTask> assemblyTasks) {
        return assemblyTasks.stream().anyMatch(at -> at.getName().equals(name));
    }

    public static AssemblyTask getAssemblyTaskByName(String name, List<AssemblyTask> assemblyTasks) {
        return assemblyTasks.stream().filter(at -> at.getName().equals(name)).findAny().orElseThrow();
    }

    public void finishTask(){
        finished = true;
    }

    @Override
    public String toString() {
        return String.format("Task %s: is %s", name, finished ? "finished" : "pending");
    }
}
