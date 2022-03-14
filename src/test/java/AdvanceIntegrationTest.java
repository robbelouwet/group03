import app.ui.AppTextView;
import app.ui.interfaces.IAppView;
import org.junit.jupiter.api.Test;

public class AdvanceIntegrationTest {
    private final IAppView view;

    public AdvanceIntegrationTest() {
        view = new AppTextView();
    }

    @Test
    public void advanceTest(){
        view.start();



    }

}
