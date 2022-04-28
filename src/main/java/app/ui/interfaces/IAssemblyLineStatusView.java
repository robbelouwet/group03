package app.ui.interfaces;

import app.utils.ConsoleReader;

import java.util.List;
import java.util.Map;

public interface IAssemblyLineStatusView {
    default void showAssemblyLineStatus(Map<String, String> pendingOrders,
                                        Map<String, List<String>> pendingTasks,
                                        Map<String, List<String>> finishedTasks){
        ConsoleReader.getInstance().printf("Current Assembly Line Status: %s\n", pendingOrders.size() > 0 ? "" : "Empty");
        printOrders(pendingOrders);

        ConsoleReader.getInstance().printf("Pending tasks of workstations: %s\n", pendingTasks.size() > 0 ? "" : "Nothing");
        printTasks(pendingTasks);

        ConsoleReader.getInstance().printf("Finished tasks of workstations: %s\n", finishedTasks.size() > 0 ? "" : "Nothing");
        printTasks(finishedTasks);
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
