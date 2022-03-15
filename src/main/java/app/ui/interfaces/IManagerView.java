package app.ui.interfaces;

import java.util.List;

public interface IManagerView {
    void confirmMove(int timeSpent);

    // TODO: pendingTasks & finishedTasks of type Map<String, List<String>>
    void showOverview(List<String> pendingOrders,
                      List<String> simFinishedOrders,
                      List<List<String>> pendingTasks,
                      List<List<String>> finishedTasks);

    void showErrorMessage(String err);
}
