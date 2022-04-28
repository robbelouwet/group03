package app.ui.interfaces;

import java.util.List;
import java.util.Map;

/**
 * Interface {@code IManagerView} provides an interface for the UI-view for the manager.
 */
public interface IManagerView extends IAssemblyLineStatusView {
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
