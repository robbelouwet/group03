package app;

import app.utils.ConsoleReader;
import app.ui.AppTextView;
import app.ui.interfaces.IAppView;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class AdvanceIntegrationTest {
    private final IAppView view;
    private final ConsoleReader mockedReader;

    public AdvanceIntegrationTest() {
        view = new AppTextView();

        ConsoleReader mReader = mock(ConsoleReader.class);
        ConsoleReader.setInstance(mReader);
        mockedReader = mReader;
    }

    @Test
    public void advanceTest() {
        when(mockedReader.ask("Who are you? [manager] | [garage holder] | [mechanic] | [quit]")).thenReturn("manager");
        when(mockedReader.ask("Advance Assembly Line? [yes] | [no]")).thenReturn("yes");
        when(mockedReader.ask("Time spent? [positive number]")).thenReturn("20");
        when(mockedReader.ask("Who are you? [manager] | [garage holder] | [mechanic] | [quit]")).thenReturn("quit");
        view.start();
    }

}
