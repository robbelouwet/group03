package domain;

import lombok.Getter;

public class AssemblyTask {
    @Getter private boolean finished;
    @Getter private String name;

    public void getTaskInformation(){}

    public void finishTask(){}
}
