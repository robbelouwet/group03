package app.ui;

import app.controllers.CheckOrderDetailsController;
import app.ui.interfaces.ICheckOrderDetailsView;
import app.utils.ConsoleReader;

import java.util.List;

public class CheckOrderDetailsTextView implements ICheckOrderDetailsView {
    private final CheckOrderDetailsController controller;

    public CheckOrderDetailsTextView(CheckOrderDetailsController controller) {
        this.controller = controller;
        controller.setUi(this);
        start();
    }

    private void start() {
        controller.start();
    }

    private boolean number(String n) {
        try {
            var number = Integer.parseInt(n);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    @Override
    public int selectCar(List<String> pendingOrders, List<String> finishedOrders) {
        ConsoleReader.getInstance().println("Pending Orders:");
        for (var order : pendingOrders) ConsoleReader.getInstance().println(order);
        ConsoleReader.getInstance().println("Finished Orders:");
        for (var order : finishedOrders) ConsoleReader.getInstance().println(order);
        var result = ConsoleReader.getInstance().ask("Select the number before an order or [cancel]");
        while (!result.equals("cancel") && !(number(result) && Integer.parseInt(result) < pendingOrders.size() + finishedOrders.size())) {
            result = ConsoleReader.getInstance().ask("Try again: ");
        }
        if (result.equals("cancel")) return -1;
        return Integer.parseInt(result);
    }

    @Override
    public boolean showDetails(String order) {
        ConsoleReader.getInstance().println(order);
        var result = ConsoleReader.getInstance().ask("Show another order? [quit] | [other]: ");
        while (!List.of("quit", "other").contains(result)) {
            result = ConsoleReader.getInstance().ask("Try again: ");
        }
        return result.equals("quit");
    }
}
