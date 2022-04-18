package app.ui.interfaces;

import app.utils.ConsoleReader;

import java.util.List;
import java.util.Map;

public interface IAssemblyLineStatusView {
    default void showAssemblyLineStatus(Map<String, String> pendingOrders,
                                        Map<String, String> simFinishedOrders,
                                        Map<String, List<String>> pendingTasks,
                                        Map<String, List<String>> finishedTasks){
        ConsoleReader.getInstance().printf("Current Assembly Line Status: %s\n", pendingOrders.size() > 0 ? "" : "Empty");
        printOrders(pendingOrders);

        ConsoleReader.getInstance().printf("Future Assembly Line Status: %s\n", simFinishedOrders.size() > 0 ? "" : "Empty");
        printOrders(simFinishedOrders);

        ConsoleReader.getInstance().printf("Pending tasks of workstations: %s\n", pendingTasks.size() > 0 ? "" : "Nothing");
        printTasks(pendingTasks);

        ConsoleReader.getInstance().printf("Finished tasks of workstations: %s\n", finishedTasks.size() > 0 ? "" : "Nothing");
        printTasks(finishedTasks);
    }

    /**
     * Shows an overview of the assembly line status after it has been moved forward with one step.
     *
     * @param pendingOrders All car orders that still need to be processed on the assembly line.
     */
    default void showAssemblyLineStatusAfterMove(Map<String, String> pendingOrders){
        ConsoleReader.getInstance().printf("Assembly Line Status after the Move: %s\n", pendingOrders.size() > 0 ? "" : "Empty");
        printOrders(pendingOrders);
    }

    private void printOrders(Map<String, String> list) {
        list.forEach((k, v) -> {
            ConsoleReader.getInstance().printf("\tWorkstation %s: ", k);
            ConsoleReader.getInstance().printf("%s\n", list.get(k));
        });
    }

    private void printTasks(Map<String, List<String>> list) {
        list.forEach((k, v) -> {
            ConsoleReader.getInstance().printf("\tWorkstation %s:\n", k);
            for (int i = 0; i < v.size(); i++) {
                ConsoleReader.getInstance().printf("\t\t%s\n", list.get(k).get(i));
            }
        });
    }
}
