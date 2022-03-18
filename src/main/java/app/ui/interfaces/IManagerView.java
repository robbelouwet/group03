package app.ui.interfaces;

import java.util.List;
import java.util.Map;

/**
 * Interface {@code IManagerView} provides an interface for the UI-view for the manager.
 */
public interface IManagerView {
    /**
     * This method will try to move the assembly line forward with one step.
     *
     * @param timeSpent The time that was spent during the current phase in minutes (normally, a phase lasts 1 hour).
     */
    void confirmMove(int timeSpent);

    /**
     * Shows an overview of the current & simulated future assembly-line status and the pending & finished assembly tasks of the assembly line.
     *
     * @param pendingOrders     All car orders that still need to be processed on the assembly line.
     * @param simFinishedOrders All car orders that would have been finished after the assembly line moves forward in normal circumstances. This is a simulation-view
     * @param pendingTasks      All assembly tasks that still need to be done in the workstations.
     * @param finishedTasks     All assembly tasks that are finished in the workstations.
     */
    void showOverview(Map<String, String> pendingOrders,
                      Map<String, String> simFinishedOrders,
                      Map<String, List<String>> pendingTasks,
                      Map<String, List<String>> finishedTasks);

    void showErrorMessage(String err);

    /**
     * Shows an overview of the assembly line status after it has been moved forward with one step.
     *
     * @param pendingOrders All car orders that still need to be processed on the assembly line.
     */
    void showAssemblyLineStatusAfterMove(Map<String, String> pendingOrders);
}
