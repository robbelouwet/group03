package app.controllers;

import app.ui.AlgorithmOptionsWrapper;
import app.ui.ConsoleStatisticsReportGenerator;
import app.ui.interfaces.IManagerView;
import domain.car.options.Option;
import domain.car.options.OptionCategory;
import domain.scheduler.SchedulingAlgorithm;
import services.AlgorithmOptions;
import services.AssemblyManager;
import services.ProductionSchedulerManager;

import java.util.*;

/**
 * Class {@code ManagerController} is responsible for the communication between the UI and the Domain.
 */
public class ManagerController extends AssemblyLineStatusController {
    private final ProductionSchedulerManager productionSchedulerManager;
    private IManagerView ui;

    ManagerController(AssemblyManager assemblyManager, ProductionSchedulerManager productionSchedulerManager) {
        super(assemblyManager);
        this.productionSchedulerManager = productionSchedulerManager;
    }

    public void setUi(IManagerView ui) {
        this.ui = ui;
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
     * @param options wrapper class to store data or fields. Some algorithms need to know which selected Car Options need priority.
     */
    public boolean selectAlgorithm(String algorithm, AlgorithmOptionsWrapper options) {
        var algorithms = productionSchedulerManager.getSchedulingAlgorithms();
        for (String alg : algorithms) {
            if (alg.equals(algorithm)) {
                var selectedOptions = options.options();
                try {
                    return productionSchedulerManager.selectAlgorithm(algorithm, new AlgorithmOptions(convertOptions(selectedOptions)));
                } catch (Exception e) {
                    ui.showErrorMessage(e.getMessage());
                    return false;
                }
            }
        }
        return false;
    }

    private Map<OptionCategory, Option> convertOptions(Map<String, String> selectedOptions) {
        Map<OptionCategory, Option> options = new HashMap<>();
        for (var cat : selectedOptions.keySet()) {
            var category = new OptionCategory(cat);
            options.put(category, new Option(category, selectedOptions.get(cat)));
        }
        return options;
    }

    /**
     * Method for the UI that retrieves the possible car selectedOptions to give priority to.
     *
     * @param algorithm The textual representation of the chosen algorithm.
     */
    public void showSpecificationBatchOrders(String algorithm) {
        List<Map<String, String>> listConversion = new ArrayList<>();
        var options = productionSchedulerManager.getPossibleOrdersForSpecificationBatch();
        for (var map : options) {
            Map<String, String> mapConversion = new LinkedHashMap<>();
            for (var key : map.keySet()) {
                mapConversion.put(key.toString(), map.get(key).toString());
            }
            listConversion.add(mapConversion);
        }
        ui.showPossibleOptionsForAlgorithm(listConversion, algorithm);
    }

    public void getStatistics() {
        ConsoleStatisticsReportGenerator reportGenerator = new ConsoleStatisticsReportGenerator(productionSchedulerManager.getStatistics());
        reportGenerator.generateReport();
        ui.showStatistics(reportGenerator.toString());
    }
}
