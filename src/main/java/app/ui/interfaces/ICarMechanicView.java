package app.ui.interfaces;

import java.util.List;

/**
 * Interface {@code ICarMechanicView} provides an interface for the UI-view for the car mechanic.
 */
public interface ICarMechanicView extends IAssemblyLineStatusView {
    /**
     * Shows an overview of the available work stations (to be worked on).
     * @param availableWorkstations A list of all the available work stations (to be worked on).
     */
    void showWorkStations(List<String> availableWorkstations);

    /**
     * Shows an overview of the available tasks.
     * @param workStationTasks A list of all the available tasks.
     */
    void showAvailableTasks(List<String> workStationTasks);

    /**
     * Show the info of a task, as well as its actions.
     * @param info The info of a task.
     * @param actions A list of all the actions of a task.
     */
    void showTaskInfo(String info, List<String> actions);
}
