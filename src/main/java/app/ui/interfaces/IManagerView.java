package app.ui.interfaces;

/**
 * Interface {@code IManagerView} provides an interface for the UI-view for the manager.
 */
public interface IManagerView extends IAssemblyLineStatusView {
    /**
     * This method will try to move the assembly line forward with one step.
     *
     * @param timeSpent The time that was spent during the current phase in minutes (normally, a phase lasts 1 hour).
     */
    void confirmMove(int timeSpent);

    /**
     * Shows an overview of the current & simulated future assembly-line status and the pending & finished assembly tasks of the assembly line.
     */
    void showAdvanceOverview();

    void showErrorMessage(String err);
}
