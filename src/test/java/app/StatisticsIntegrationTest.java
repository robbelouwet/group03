package app;

import app.ui.AppTextView;
import app.utils.ConsoleReader;
import app.utils.IConsoleReader;
import org.junit.jupiter.api.Test;
import persistence.CarOrderRepository;
import persistence.DataSeeder;

import static org.mockito.Mockito.*;

public class StatisticsIntegrationTest {

    public StatisticsIntegrationTest() {
        ConsoleReader.setInstance(new IConsoleReader() {
            int number = 0;
            @Override
            public String ask(String str) {
                return switch (number++){
                    case 0 -> "manager";
                    case 1 -> "statistics";
                    default -> "quit";
                };
            }

            @Override
            public void println(String l) {

            }

            @Override
            public void print(String s) {

            }

            @Override
            public void printf(String format, Object obj) {

            }
        });
    }

    @Test
    public void mainScenarioTest() {
        var testOrders = DataSeeder.delayedTestOrders();
        var repo = mock(CarOrderRepository.class);
        when(repo.getOrders()).thenReturn(testOrders);

        var view = new AppTextView();
        view.start();
    }
}
