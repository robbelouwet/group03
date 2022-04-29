package services;

import domain.scheduler.DateTime;

public record Statistics(Long lastDelay, DateTime lastDelayDate, Long secondLastDelay, DateTime secondLastDelayDate,
                         float medianDelay, float averageDelay, long ordersFinishedYesterday,
                         long ordersFinishedDayBefore,
                         long medianFinishedPerDay, float averageFinishedPerDay) {

    @Override
    public String toString() {
        return "Last delay was " + lastDelay() + " minutes, at " + lastDelayDate() + " minutes\n" +
                "Second last delay was " + secondLastDelay() + " minutes, at " + secondLastDelayDate() + " minutes\n" +
                "The median amount of delay was: " + medianDelay() + " minutes, the average was " + averageDelay() + " minutes\n" +
                "Yesterday, " + ordersFinishedYesterday() + " orders got finished\n" +
                "The day before that: " + ordersFinishedDayBefore() + "\n" +
                "The median amount of finished orders per day is: " + medianFinishedPerDay() + ", the average is " + averageFinishedPerDay() + "\n";
    }
}
