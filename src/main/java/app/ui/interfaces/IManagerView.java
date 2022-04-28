package app.ui.interfaces;

import java.util.List;
import java.util.Map;

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

    /**
     * This method shows all the available scheduling algorithms to the manager.
     * @param algorithms list of textual representations of the algorithms
     * @param selectedAlgorithm the current algorithm that is chosen
     *                          and is performing the task of scheduling the orders
     */
    void showSchedulingAlgorithms(List<String> algorithms, String selectedAlgorithm);

    /**
     * This method shows the Car Options that can be chosen to give priority in the algorithm
     * @param options A list of Key-Value pairs for the selected car options (Car Option - value)
     *                This is not the same as all the possible options for an option, rather 1 selected
     *                value for a Car Option Category.
     * @param algorithm The scheduling algorithm that has been chosen to change to.
     */
    void showPossibleOptionsForAlgorithm(List<Map<String, String>> options, String algorithm);

    void showStatistics(String statistics);
}
