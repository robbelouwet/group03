package app.controllers;

import app.ui.interfaces.IManagerView;
import domain.scheduler.SchedulingAlgorithm;
import services.AssemblyManager;
import services.ProductionSchedulerManager;
import services.Statistics;

import java.util.*;

/**
 * Class {@code ManagerController} is responsible for the communication between the UI and the Domain.
 */
public class ManagerController extends AssemblyLineStatusController {
    private final AssemblyManager assemblyManager;
    private final ProductionSchedulerManager productionSchedulerManager;
    private IManagerView ui;

    ManagerController(AssemblyManager assemblyManager, ProductionSchedulerManager productionSchedulerManager) {
        super(assemblyManager);
        this.assemblyManager = assemblyManager;
        this.productionSchedulerManager = productionSchedulerManager;
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
        //boolean success = assemblyManager.startDay();
        if (false) ui.showErrorMessage("Assembly line is blocked!");

        var ordersOnAssembly = assemblyManager.getOrdersOnAssemblyLine();
        showAssemblyLineStatusAfterMove(ordersOnAssembly, ui);
    }

    /**
     * Method for the UI that retrieves the currently selected scheduling algorithm
     * and all the available algorithms.
     */
    public void showAlgorithmOverview() {
        SchedulingAlgorithm algorithm = productionSchedulerManager.getCurrentAlgorithm();
        ui.showSchedulingAlgorithms(productionSchedulerManager.getSchedulingAlgorithms(), algorithm.toString());
    }

    /**
     * The user has chosen an algorithm and it has to be altered now.
     *
     * @param algorithm       The textual representation of the chosen algorithm.
     * @param selectedOptions Optional of selectedOptions. Some algorithms need to know which selected
     *                        Car Options need priority.
     *                        Will be Optional.empty() if the algorithm doesn't need this.
     */
    public boolean selectAlgorithm(String algorithm, Optional<Map<String, String>> selectedOptions) {
        var algorithms = productionSchedulerManager.getSchedulingAlgorithms();
        for (String alg : algorithms) {
            if (alg.equals(algorithm)) {
                return productionSchedulerManager.selectAlgorithm(algorithm, selectedOptions);
            }
        }
        return false;
    }

    /**
     * Method for the UI that retrieves the possible car options to give priority to.
     *
     * @param algorithm The textual representation of the chosen algorithm.
     */
    public void showSpecificationBatchOrders(String algorithm) {
        List<Map<String, String>> listConversion = new ArrayList<>();
        var options = productionSchedulerManager.getPossibleOrdersForSpecificationBatch();
        for (var map : options){
            Map<String, String> mapConversion = new LinkedHashMap<>();
            for (var key : map.keySet()){
                   mapConversion.put(key.toString(), map.get(key).toString());
            }
            listConversion.add(mapConversion);
        }
        ui.showPossibleOptionsForAlgorithm(listConversion, algorithm);
    }

    public void getStatistics(){
        var stats = productionSchedulerManager.getStatistics();
        if (stats != null)
            ui.showStatistics(stats.toString());
        else
            ui.showErrorMessage("No Cars were produced!");
    }
}
