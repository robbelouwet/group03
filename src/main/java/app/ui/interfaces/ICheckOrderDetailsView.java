package app.ui.interfaces;

import java.util.List;

public interface ICheckOrderDetailsView {
    /**
     * @param pendingOrders  the orders to pick from with leading digit
     * @param finishedOrders the orders to pick from with leading digit
     * @return the number of the order you want to see. -1 if user wants to quit
     */
    int selectCar(List<String> pendingOrders, List<String> finishedOrders);

    /**
     * @param order details
     * @return true if the user wants to quit the use case, false if user wants to see other carorder
     */
    boolean showDetails(String order);
}
