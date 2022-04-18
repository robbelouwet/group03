package domain.scheduler;

import lombok.Getter;

public class TimeManager {
    /**
     * A field that represents the time of our application
     * The application always starts at 6 in the morning
     */
    @Getter
    private DateTime currentTime;

    public TimeManager() {
        this(new DateTime(60 * 6));
    }

    public TimeManager(DateTime currentTime) {
        this.currentTime = currentTime;
    }

    /**
     * Increment the current time of our application
     *
     * @param minutes the amount of minutes to add to the current time
     */
    void addTime(long minutes) {
        currentTime = currentTime.addTime(minutes);
    }

    /**
     * @return A copy of this object
     */
    public TimeManager copy() {
        return new TimeManager(currentTime);
    }
}
