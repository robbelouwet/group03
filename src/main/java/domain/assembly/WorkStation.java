package domain.assembly;

import domain.order.CarOrder;
import domain.order.OrderStatus;
import domain.scheduler.DateTime;
import lombok.Getter;

import java.util.ArrayList;
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
    private final List<WorkStationListener> listeners = new ArrayList<>();

    /**
     * @return {@code List&#60;AssemblyTask&#62;} all assembly tasks that are assigned to a {@code WorkStation} that still need to be done.
     */
    public List<AssemblyTask> getPendingTasks() {
        if (currentOrder == null) return new ArrayList<>();
        return tasks.stream().filter(t -> !t.isFinished(currentOrder)).collect(Collectors.toList());
    }

    public List<AssemblyTask> getFinishedTasks() {
        if (currentOrder == null) return tasks;
        return tasks.stream().filter(t -> t.isFinished(currentOrder)).collect(Collectors.toList());
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
        return currentOrder == null || tasks.stream().allMatch(t -> t.isFinished(currentOrder));
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
    public void finishCarOrder(DateTime currentTime) {
        if (currentOrder != null) {
            currentOrder.setStatus(OrderStatus.Finished);
            currentOrder.setEndTime(currentTime);
            var order = currentOrder.copy();
            currentOrder = null;
        }
    }

    public WorkStation copy() {
        return new WorkStation(name, tasks, currentOrder);
    }

    @Override
    public String toString() {
        return "Workstation: [" + this.getName() + "]";
    }

    public List<String> getTasksInformation() {
        return getPendingTasks().stream().map(t -> t.getInformation(currentOrder)).collect(Collectors.toList());
    }

    public List<String> getTasksInformation(List<AssemblyTask> tasks){
        return tasks.stream().map(t -> t.getInformation(currentOrder)).collect(Collectors.toList());
    }

    public void addListener(WorkStationListener listener) {
        listeners.add(listener);
    }

    public void removeListener(WorkStationListener listener){
        listeners.remove(listener);
    }

    public void finishTask(AssemblyTask task, int timeSpent){
        if (!tasks.contains(task)) throw new IllegalStateException("The task you're trying to finish is not linked with this workstation.");
        task.finishTask(timeSpent);

        int accumulatedTimeSpentAtWorkStation = tasks.stream().mapToInt(AssemblyTask::getTimeSpent).sum();

        for(WorkStationListener listener: new ArrayList<>(listeners)){
            listener.finishedTask(accumulatedTimeSpentAtWorkStation);
        }
    }
}