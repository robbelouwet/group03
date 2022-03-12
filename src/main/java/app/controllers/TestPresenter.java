package app.controllers;

import app.ui.TestFrame;
import services.assembly.AssemblyManager;
import services.car.CarOrderManager;

public class TestPresenter {

    private final CarOrderManager carOrderManager = CarOrderManager.getInstance();
    private final AssemblyManager assemblyManager = AssemblyManager.getInstance();

    private TestFrame testFrame;

    public TestPresenter(TestFrame view) {
        this.testFrame = view;
    }

    public void testEvent(){
        assemblyManager.advance(10);
        testFrame.updateSuccess("Success!");

    }
}
