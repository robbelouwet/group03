package app.controllers;

import app.ui.interfaces.ICheckOrderDetailsView;
import domain.order.CarOrder;
import services.CarOrderManager;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CheckOrderDetailsController {
    private final CarOrderManager manager;
    private ICheckOrderDetailsView ui;

    public CheckOrderDetailsController(CarOrderManager manager) {
        this.manager = manager;
    }

    public void setUi(ICheckOrderDetailsView ui) {
        this.ui = ui;
    }

    public void start() {
        while(true) {
            var pendingOrders = manager.getPendingOrders();
            var finishedOrders = manager.getFinishedOrders();
            var pending = IntStream.range(0, pendingOrders.size()).mapToObj(i -> i + ": " + pendingOrders.get(i).toString()).collect(Collectors.toList());
            var finished = IntStream.range(0, finishedOrders.size()).mapToObj(i -> (i + pending.size()) + ": " + finishedOrders.get(i).toString()).collect(Collectors.toList());
            var index = ui.selectCar(pending, finished);
            if (index == -1) {
                return;
            }
            boolean quit;
            if (index > pending.size() - 1) {
                quit = showOrder(finishedOrders.get(index - pending.size()));
            } else {
                quit = showOrder(pendingOrders.get(index));
            }
            if (quit) return;
        }
    }

    private boolean showOrder(CarOrder order) {
        StringBuilder o = new StringBuilder(order.toString() + "\n");
        for (var opt : order.getSelections().entrySet()) {
            o.append(opt.getKey()).append(": ").append(opt.getValue()).append("\n");
        }
        return ui.showDetails(o.toString());
    }

}
