package domain.car;

import domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Setter
public class CarOrder {
    @Getter
    private LocalDateTime startTime, endTime;
    @Getter
    private Car car;
    @Getter
    private OrderStatus status;

    /**
     * @throws IllegalArgumentException | data == null || startTime == null
     * @creates | getCar()
     */
    public CarOrder(LocalDateTime startTime, CarModel carModel, Map<String, String> data) {
        if (data == null || startTime == null) {
            throw new IllegalStateException();
        }
        this.startTime = startTime;
        status = OrderStatus.Pending;
        car = new Car(carModel, data);
    }

    private CarOrder(LocalDateTime startTime, LocalDateTime endTime, OrderStatus status, Car car) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.car = car;
    }

    @Override
    public CarOrder clone() {
        //TODO: clone start- and endtime?
        return new CarOrder(startTime, endTime, status, car);
    }

    @Override
    public String toString() {
        return "CarOrder{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", finished=" + isFinished() +
                ", car=" + car +
                ", status=" + status +
                '}';
    }

    public boolean isFinished() {
        return status.equals(OrderStatus.Finished);
    }
}
