package app.ui.interfaces;

import java.util.List;
import java.util.Map;

public interface IManagerView {
    void confirmMove(int timeSpent);

    // TODO: pendingTasks & finishedTasks of type Map<String, List<String>>
    void showOverview(List<String> pendingOrders,
                      List<String> simFinishedOrders,
                      Map<String, List<String>> pendingTasks,
                      Map<String, List<String>> finishedTasks);

    void showAssemblyLineStatusAfterMove(List<String> pendingOrders);
}
