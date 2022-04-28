package domain.scheduler;

import lombok.Getter;

public class TimeManager {
    /**
     * A field that represents the time of our application
     * The application always starts at 6 in the morning
     */
    @Getter
    private DateTime currentTime;
    @Getter
    private int timeSpentOnThisStep = 0;

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

    /**
     * Advance the time to the start of the next day
     */
    public void nextDay() {
        if (currentTime.getMinutesInDay() <= 6*60) currentTime = new DateTime(currentTime.getDays(), 6, 0);
        else currentTime = new DateTime(currentTime.getDays() + 1, 6, 0);
    }

    public void timePassedOnStep(int timeSpent) {
        if (timeSpent > timeSpentOnThisStep) {
            currentTime = currentTime.addTime(timeSpent - timeSpentOnThisStep);
            timeSpentOnThisStep = timeSpent;
        }
    }

    public void resetStep() {
        timeSpentOnThisStep = 0;
    }
}
