package domain.order;

import domain.car.Car;
import domain.car.CarModel;
import domain.time.DateTime;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * This class represents an order that was placed by a garage holder.
 * During its lifecycle, its predicted end time and status will be updated.
 */
public class CarOrder {
    private final DateTime startTime;
    @Setter
    private DateTime endTime;
    @Getter
    private final Car car;
    @Getter
    @Setter
    private OrderStatus status;

    /**
     * @param startTime the time the order was created, should not be null
     * @param carModel  the model of this car
     * @param data      the selection op options, a map which maps the option-name to its selected value
     */
    public CarOrder(DateTime startTime, CarModel carModel, Map<String, String> data) {
        if (data == null || startTime == null || carModel == null) {
            throw new IllegalArgumentException();
        }

        this.startTime = startTime;
        status = OrderStatus.Pending;
        car = new Car(carModel, data);
    }

    private CarOrder(DateTime startTime, DateTime endTime, Car car, OrderStatus status) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.car = car;
        this.status = status;
    }

    /**
     * Creates a copy of this object, which makes it impossible to change the representation of the current object
     */
    public CarOrder copy() {
        // We take a shallow copy, since all representation objects are immutable
        return new CarOrder(startTime, endTime, car, status);
    }

    @Override
    public String toString() {
        return "Order (" + car.getModel().getName() + "): " + "startTime=" + startTime + ", endTime=" + endTime  + ", status=" + status + '}';
    }

    /**
     * @return getStatus() == OrderStatus.Finished
     */
    public boolean isFinished() {
        return status.equals(OrderStatus.Finished);
    }

    /**
     * The time at which the order was created
     */
    public DateTime getStartTime() {
        return startTime;
    }

    /**
     * If the order is not finished, this represents the expected end time
     * If the order is finished, this represents the time at which the order was finished
     */
    public DateTime getEndTime() {
        return endTime;
    }
}
