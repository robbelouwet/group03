package domain.assembly;

import domain.car.CarOrder;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class AssemblyProcess {
    private List<AssemblyTask> tasks;
    private final CarOrder carOrder;

    public AssemblyProcess(CarOrder carOrder) {
        this.carOrder = carOrder;
    }

    public List<AssemblyTask> getTasks() {
        throw new UnsupportedOperationException();
    }

    public CarOrder getCarOrder() {
        throw new UnsupportedOperationException();
    }

    public LocalDateTime getStartTimeOrder() {
        throw new UnsupportedOperationException();
    }
}
