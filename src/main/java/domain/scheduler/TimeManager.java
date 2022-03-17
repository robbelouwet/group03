package domain.scheduler;

import lombok.Getter;

public class TimeManager {
    /**
     * A field that represents the time of our application
     * The application always starts at 6 in the morning
     */
    @Getter
    private static DateTime currentTime = new DateTime(60 * 6);

    /**
     * Increment the current time of our application
     *
     * @param minutes the amount of minutes to add to the current time
     */
    static void addTime(long minutes) {
        currentTime = currentTime.addTime(minutes);
    }
}
