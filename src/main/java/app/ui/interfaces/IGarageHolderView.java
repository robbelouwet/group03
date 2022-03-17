package app.ui.interfaces;

import domain.scheduler.DateTime;

import java.util.List;
import java.util.Map;

public interface IGarageHolderView{

    void showOverview(List<String> pendingOrders, List<String> finishedOrders);

    void showCarModels(List<String> models);

    void showCarForm(Map<String, List<String>> options);

    void showPredictedEndTime(DateTime endTime);
}
