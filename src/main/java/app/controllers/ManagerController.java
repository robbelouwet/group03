package app.controllers;

import app.ui.interfaces.IManagerView;
import services.AssemblyManager;

import java.util.List;

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
        // TODO: not hard-coded -> retrieve via assemblyManager
        ui.showSchedulingAlgorithms(List.of("FIFO", "Specification Batch"), "FIFO");
    }

    public void selectAlgorithm(String algorithm) {
        // TODO: map String to a SchedulingAlgorithm so that we can change the instance
        // List<?> algorithms = assemblyManager.getSchedulingAlgorithms();
        // for (? alg in algorithms){
        //     if (algorithm.equals(algorithm.getName())){
        //         assemblyManager.select(algorithm);
        //     }
        // }
    }
}
