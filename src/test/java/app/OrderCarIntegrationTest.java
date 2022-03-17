package app;

import app.ui.AppTextView;
import app.ui.interfaces.IAppView;
import app.utils.ConsoleReader;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderCarIntegrationTest {
    private final IAppView view;
    private final ConsoleReader mockedReader;

    public OrderCarIntegrationTest() {
        view = new AppTextView();

        ConsoleReader mReader = mock(ConsoleReader.class);
        ConsoleReader.setInstance(mReader);
        mockedReader = mReader;
    }

    @Test
    public void advanceTest() {
        when(mockedReader.ask("Who are you? [manager] | [garage holder] | [mechanic] | [quit]")).thenReturn("garage holder");
        view.start();
    }
}
