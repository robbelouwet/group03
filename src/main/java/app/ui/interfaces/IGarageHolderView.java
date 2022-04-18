package app.ui.interfaces;

import domain.scheduler.DateTime;

import java.util.List;
import java.util.Map;

public interface IGarageHolderView {

    boolean showOverview(List<String> pendingOrders, List<String> finishedOrders);

    String showCarModels(List<String> models);

    String showCarOption(String category, List<String> options);

    boolean confirmOrder();

    void showPredictedEndTime(DateTime endTime);
}
