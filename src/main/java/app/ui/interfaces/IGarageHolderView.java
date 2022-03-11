package app.ui.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IGarageHolderView {
    void showOverview(List<String> pendingOrders, List<String> finishedOrders);

    boolean isModelName(String name, List<String> models);

    void showCarModels(List<String> models);

    boolean isValue(String value, List<String> values);

    void showCarForm(Map<String, List<String>> options);

    void showPredictedEndTime(LocalDateTime endTime);
}
