package domain.assembly;

import domain.order.CarOrder;
import domain.order.OrderStatus;
import domain.scheduler.DateTime;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class {@code WorkStation} contains assembly tasks that need to be performed on the current {@code CarOrder}.
 * It also contains this current {@code CarOrder}.
 */
public class WorkStation {
    @Getter
    private final String name;
    private CarOrder currentOrder;
    private final List<AssemblyTask> tasks;

    /**
     * @return {@code List&#60;AssemblyTask&#62;} all assembly tasks that are assigned to a {@code WorkStation} that still need to be done.
     */
    public List<AssemblyTask> getPendingTasks() {
        return tasks.stream().filter(t -> !t.isFinished()).collect(Collectors.toList());
    }

    /**
     * @return {@code List&#60;AssemblyTask&#62;} all assembly tasks that are assigned to a {@code WorkStation} and are finished.
     */
    public List<AssemblyTask> getAllTasks() {
        return tasks;
    }

    /**
     * This method will reset all the assembly tasks of a workstation back to not finished.
     */
    public void resetAllTasks(){
        tasks.forEach(AssemblyTask::resetTask);
    }

    /**
     * @param name  The name of the {@code WorkStation}.
     * @param tasks The assembly tasks of the {@code WorkStation}.
     */
    public WorkStation(String name, List<AssemblyTask> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    private WorkStation(String name, List<AssemblyTask> tasks, CarOrder currentOrder) {
        this.name = name;
        if (currentOrder != null) {
            this.currentOrder = currentOrder.copy();
        } else {
            this.currentOrder = null;
        }
        this.tasks = tasks.stream().map(AssemblyTask::copy).collect(Collectors.toList());
    }

    /**
     * @return {@code CarOrder} The current {@code CarOrder} on which assembly tasks are performed.
     */
    public CarOrder getCarOrder() {
        return currentOrder;
    }

    /**
     * @return true if the current {@code CarOrder} is not null or is finished
     * @see domain.order.CarOrder#isFinished()
     */
    public boolean hasCompleted() {
        return currentOrder == null || tasks.stream().allMatch(AssemblyTask::isFinished);
    }

    /**
     * @param currentTime The current {@code DateTime}
     * @see domain.order.CarOrder#setEndTime(DateTime currentTime)
     */
    public void updateEndTimeOrder(DateTime currentTime) {
        if (currentOrder != null)
            currentOrder.setEndTime(currentTime);
    }

    /**
     * @param order A {@code CarOrder}
     */
    public void updateCurrentOrder(CarOrder order) {
        currentOrder = order;
    }

    /**
     * This method sets the current car order on finished if there is one present.
     */
    public CarOrder finishCarOrder() {
        if (currentOrder != null) {
            currentOrder.setStatus(OrderStatus.Finished);
            var order = currentOrder.copy();
            currentOrder = null;
            return order;
        }
        return null;
    }

    public WorkStation copy() {
        return new WorkStation(name, tasks, currentOrder);
    }

    @Override
    public String toString() {
        return "Workstation: [" + this.getName() + "]";
    }
}