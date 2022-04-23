package app.ui.interfaces;

import domain.scheduler.DateTime;

import java.util.List;

public interface IOrderNewCarView {

    /**
     * Show the overview
     *
     * @param pendingOrders  Which orders are pending
     * @param finishedOrders Which orders are finished
     * @return whether we should exit the garage holder or not
     */
    boolean showOverview(List<String> pendingOrders, List<String> finishedOrders);

    /**
     * Show the available car models to choose from
     *
     * @param models The available models
     * @return The selected model
     */
    String showCarModels(List<String> models);

    /**
     * Show the available options for a category
     *
     * @param category The category
     * @param options  All available options
     * @return The option that was selected, or null if cancelled
     */
    String showCarOption(String category, List<String> options);

    /**
     * Ask the user to confirm the order
     *
     * @return whether the user confirmed or cancelled
     */
    boolean confirmOrder();

    /**
     * Show the predicted end time to the user
     *
     * @param endTime The predicted end time
     */
    void showPredictedEndTime(DateTime endTime);
}
