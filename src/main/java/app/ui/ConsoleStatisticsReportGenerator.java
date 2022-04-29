package app.ui;

import services.Statistics;
import services.StatisticsReportGenerator;

public class ConsoleStatisticsReportGenerator extends StatisticsReportGenerator {
    private final StringBuilder builder = new StringBuilder();

    public ConsoleStatisticsReportGenerator(Statistics statistics) {
        super(statistics);
    }

    @Override
    protected void addTitle(String title) {
        builder.append(title.toUpperCase());
        builder.append("\n");
        builder.append("\n");
    }

    @Override
    protected void addSubtitle(String subtitle) {
        builder.append(subtitle);
        builder.append("\n");
    }

    @Override
    protected void startBullets() {

    }

    @Override
    protected void endBullets() {

    }

    @Override
    protected void addBullet(String line) {
        builder.append("- ");
        builder.append(line);
        builder.append("\n");
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
