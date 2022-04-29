package services;

public abstract class StatisticsReportGenerator {
    private final Statistics statistics;

    public StatisticsReportGenerator(Statistics statistics) {
        this.statistics = statistics;
    }

    protected abstract void addTitle(String title);

    protected abstract void addSubtitle(String subtitle);

    protected abstract void startBullets();

    protected abstract void endBullets();

    protected abstract void addBullet(String line);

    public void generateReport() {
        addTitle("Statistics");

        if (statistics.lastDelay() == null) {
            addSubtitle("No orders were finished!");
        } else {
            addSubtitle("Recent delays");
            startBullets();
            addBullet("Last delay was " + statistics.lastDelay() + " minutes, at " + statistics.lastDelayDate());
            addBullet("Second last delay was " + statistics.secondLastDelay() + " minutes, at " + statistics.secondLastDelayDate() + " minutes");
            endBullets();
            addSubtitle("Average and median");
            startBullets();
            addBullet("The median amount of delay was: " + statistics.medianDelay() + " minutes, the average was " + statistics.averageDelay() + " minutes");
            addBullet("The median amount of finished orders per day is: " + statistics.medianFinishedPerDay() + ", the average is " + statistics.averageFinishedPerDay());
            endBullets();
            addSubtitle("Recent finished");
            startBullets();
            addBullet("Yesterday, " + statistics.ordersFinishedYesterday() + " orders got finished");
            addBullet("The day before that: " + statistics.ordersFinishedDayBefore());
            endBullets();
        }
    }
}
