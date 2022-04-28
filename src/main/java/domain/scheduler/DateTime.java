package domain.scheduler;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * An immutable class that represents time in our application.
 */
public class DateTime implements Comparable<DateTime> {
    private final long minutes;

    public DateTime(long minutes) {
        this.minutes = minutes;
    }

    /**
     * Create a datetime object based on the days, minutes and hours
     */
    public DateTime(long days, long hours, long minutes) {
        this.minutes = days * 24 * 60 + hours * 60 + minutes;
    }

    /**
     * @param minutes the amount of minutes to add
     * @return a new DateTime object that is `minutes` minutes later than the time of `this`
     */
    public DateTime addTime(long minutes) {
        return new DateTime(this.minutes + minutes);
    }

    /**
     * @param minutes the amount of minutes to subtract
     * @return a new DateTime object that is `minutes` minutes before the time of `this`
     */
    public DateTime subtractTime(long minutes) {
        return new DateTime(this.minutes - minutes);
    }

    /**
     * @return the amount of days `this `represents
     */
    public long getDays() {
        return minutes / (24 * 60);
    }

    /**
     * @return the amount of hours in the current day `this `represents
     */
    public long getHours() {
        return (minutes % (24 * 60)) / 60;
    }

    /**
     * @return the amount of minutes in the current hour `this `represents
     */
    public long getMinutes() {
        return minutes % 60;
    }

    /**
     * @return the total amount of minutes passed in the current day `this` represents
     */
    public long getMinutesInDay() {
        return minutes % (24 * 60);
    }

    @Override
    public String toString() {
        return "Day " + getDays() + ", " + String.format("%02d", getHours()) + ":" + String.format("%02d", getMinutes());
    }

    @Override
    public int compareTo(DateTime o) {
        return (int) (minutes - o.minutes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateTime dateTime = (DateTime) o;
        return minutes == dateTime.minutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minutes);
    }
}
