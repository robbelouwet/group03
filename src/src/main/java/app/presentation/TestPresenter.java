package app.presentation;

import app.ui.TestFrame;
import services.AssemblyManager;
import services.CarOrderManager;

public class TestPresenter {

    private final CarOrderManager carOrderManager = CarOrderManager.getInstance();
    private final AssemblyManager assemblyManager = AssemblyManager.getInstance();

    private TestFrame testFrame;

    public TestPresenter(TestFrame view) {
        this.testFrame = view;
    }

    public void testEvent(){
        assemblyManager.advance();
        testFrame.updateSuccess("Success!");

    }
}
