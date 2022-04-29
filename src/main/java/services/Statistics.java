package services;

import domain.scheduler.DateTime;

public record Statistics(Long lastDelay, DateTime lastDelayDate, Long secondLastDelay, DateTime secondLastDelayDate,
                         float medianDelay, float averageDelay, long ordersFinishedYesterday,
                         long ordersFinishedDayBefore,
                         float medianFinishedPerDay, float averageFinishedPerDay) {
}
