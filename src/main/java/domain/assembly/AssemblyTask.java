package domain.assembly;

import domain.car.options.Option;
import domain.car.options.OptionCategory;
import domain.order.CarOrder;
import lombok.Getter;


import java.util.ArrayList;
import java.util.List;

/**
 * Class {@code AssemblyTask} is the representation of a
 * task that needs to be performed on a {@code CarOrder} on the {@code AssemblyLine}.
 */
public class AssemblyTask {
    private boolean finished;
    @Getter
    private final String name;
    @Getter
    private final List<String> actions;
    @Getter
    private int timeSpent;
    private final OptionCategory category;

    /**
     * @param name The name of the {@code AssemblyTask} that needs to be performed at a {@code WorkStation}
     */
    public AssemblyTask(String name, List<String> actions, OptionCategory category) {
        this(name, actions, category, false);
    }

    private AssemblyTask(String name, List<String> actions, OptionCategory category, boolean finished) {
        this.name = name;
        this.finished = finished;
        this.actions = new ArrayList<>(actions);  // Make a copy
        this.category = category;
    }

    public void finishTask(int timeSpent) {
        this.timeSpent = timeSpent;
        finished = true;
    }

    public boolean isFinished(CarOrder order) {
        return finished || order.getSelections().get(category).equals(new Option(category, "None"));
    }

    public void resetTask(){
        timeSpent = 0;
        finished = false;
    }

    public String getInformation(CarOrder order) {
        return String.format("Task [%s]: %s (%s)", name, order.getSelections().get(category).name(), isFinished(order) ? "finished" : "pending");
    }

    public AssemblyTask copy() {
        return new AssemblyTask(name, actions, category, finished);
    }
}
