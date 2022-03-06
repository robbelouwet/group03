package domain;

import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class AssemblyProcess {
    private ArrayList<AssemblyTask> tasks;
    @Setter private CarOrder carOrder;

    public ArrayList<AssemblyTask> getTasks() {
        throw new UnsupportedOperationException();
    }

    public CarOrder getCarOrder() {
        throw new UnsupportedOperationException();
    }

    public LocalDateTime getStartTimeOrder() {
        throw new UnsupportedOperationException();
    }

    public void updateEndTimeOrder(LocalDateTime dateTime) {
    }

    public void removeCarOrder() {
    }

}
