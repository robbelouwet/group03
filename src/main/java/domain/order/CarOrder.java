package domain.order;

import domain.car.CarModel;
import domain.car.options.Option;
import domain.car.options.OptionCategory;
import domain.scheduler.DateTime;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class represents an order that was placed by a garage holder.
 * During its lifecycle, its predicted end time and status will be updated.
 */
public class CarOrder {
    @Getter
    @Setter
    private DateTime orderTime;
    @Getter
    @Setter
    private DateTime startTime;
    @Setter
    private DateTime endTime;
    private final Map<OptionCategory, Option> selections;
    @Getter
    private final CarModel model;
    @Getter
    @Setter
    private OrderStatus status;

    /**
     * @param carModel  the model of this car
     * @param data      the selection op options, a map which maps the option-name to its selected value
     */
    public CarOrder(CarModel carModel, Map<OptionCategory, Option> data) {
        if (data == null || carModel == null) {
            throw new IllegalArgumentException();
        }
        if (!carModel.isValidInputData(data)) {
            throw new IllegalArgumentException("The data object does not match the modelspecification!");
        }
        status = OrderStatus.Pending;

        selections = data;
        model = carModel;
    }

    private CarOrder(DateTime orderTime, DateTime startTime, DateTime endTime, CarModel model, Map<OptionCategory, Option> selections, OrderStatus status) {
        this.orderTime = orderTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.model = model;
        this.selections = copySelections(selections);
        this.status = status;
    }

    /**
     * Creates a copy of this object, which makes it impossible to change the representation of the current object
     */
    public CarOrder copy() {
        return new CarOrder(orderTime, startTime, endTime, model, selections, status);
    }

    @Override
    public String toString() {
        return "Order (" + model.getName() + "): " + "orderTime=" + orderTime + ", endTime=" + endTime + ", status=" + status + '}';
    }

    /**
     * @return getStatus() == OrderStatus.Finished
     */
    public boolean isFinished() {
        return status.equals(OrderStatus.Finished);
    }

    /**
     * If the order is not finished, this represents the expected end time
     * If the order is finished, this represents the time at which the order was finished
     */
    public DateTime getEndTime() {
        return endTime;
    }

    private Map<OptionCategory, Option> copySelections(Map<OptionCategory, Option> selections) {
        return new LinkedHashMap<>(selections);
    }

    /**
     * @return the concrete selection of options, a map which maps the option-name to its value
     */
    public Map<OptionCategory, Option> getSelections() {
        return copySelections(selections);
    }

    /**
     * @return How long do we expect this order to take at every WorkStation
     */
    public long getExpectedMinutesPerWorkStation() {
        return model.getExpectedMinutesPerWorkStation();
    }
}
