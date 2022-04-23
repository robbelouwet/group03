package app.controllers;

import app.ui.interfaces.IManagerView;
import domain.scheduler.SchedulingAlgorithm;
import services.AssemblyManager;

import java.util.Map;
import java.util.Optional;

/**
 * Class {@code ManagerController} is responsible for the communication between the UI and the Domain.
 */
public class ManagerController extends AssemblyLineStatusController {
    private final AssemblyManager assemblyManager;
    private IManagerView ui;

    ManagerController(AssemblyManager assemblyManager) {
        super(assemblyManager);
        this.assemblyManager = assemblyManager;
    }

    public void setUi(IManagerView ui) {
        this.ui = ui;
    }

    /**
     * Provides the UI with the pending & finished tasks and orders of the assembly line.
     */
    public void showAssemblyLineOverview() {
        showAssemblyLineStatus(ui);
        ui.showAdvanceOverview();
    }

    /**
     * This method will try to advance the assembly line forward with one step.
     * If the assembly line has been successfully moved forward, this method will show an overview of the new assembly line status.
     *
     * @param timeSpent The time that was spent during the current phase in minutes (normally, a phase lasts 1 hour).
     */
    public void advanceAssemblyLine(int timeSpent) {
        boolean success = assemblyManager.advance(timeSpent);
        if (!success) ui.showErrorMessage("Assembly line is blocked!");

        var ordersOnAssembly = assemblyManager.getOrdersOnAssemblyLine();
        showAssemblyLineStatusAfterMove(ordersOnAssembly, ui);
    }

    public void showAlgorithmOverview() {
        SchedulingAlgorithm algorithm = assemblyManager.getCurrentAlgorithm();
        ui.showSchedulingAlgorithms(assemblyManager.getSchedulingAlgorithms(), algorithm.toString());
    }

    public void selectAlgorithm(String algorithm, Optional<Map<String, String>> selectedOptions) {
        var algorithms = assemblyManager.getSchedulingAlgorithms();
        for (String alg : algorithms) {
            if (alg.equals(algorithm)) {
                assemblyManager.selectAlgorithm(algorithm, selectedOptions);
            }
        }
    }

    public void showSpecificationBatchOrders(String algorithm) {
        var options = assemblyManager.getPossibleOrdersForSpecificationBatch();
        ui.showPossibleOptionsForAlgorithm(options, algorithm);
    }
}
