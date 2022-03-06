package domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
public class CarOrder {
    private LocalDateTime startTime, endTime;
    private boolean finished;
    private Car car;
    @Getter
    private OrderStatus status;

    public CarOrder(LocalDateTime startTime, LocalDateTime endTime, boolean finished, Car car) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.finished = finished;
        this.car = car;
    }

    @Override
    public CarOrder clone() {
        //TODO: clone start- and endtime?
        return new CarOrder(startTime, endTime, finished, car.clone());
    }
}
