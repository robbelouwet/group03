package app.ui;

import app.controllers.MechanicController;
import app.ui.interfaces.ICarMechanicView;
import domain.WorkStation;
import domain.assembly.AssemblyTask;
import services.assembly.AssemblyManager;

import java.util.List;

public class CarMechanicTextView implements ICarMechanicView {
    // TODO: hoort de assemblyManager hier? Niet gewoon MechanicController?
    private final AssemblyManager assemblyManager = AssemblyManager.getInstance();
    private final MechanicController controller;

    public CarMechanicTextView() {
        // TODO: create controller and/or view?
        controller = new MechanicController(this);
    }

    @Override
    public void showWorkStations(List<WorkStation> availableWorkstations) {
        System.out.println("Available workstations:");
        for (WorkStation ws: availableWorkstations){
            System.out.println(ws);
        }
    }

    @Override
    public void showAvailableTasks(List<AssemblyTask> workStationTasks) {

    }

    @Override
    public void showTaskInfo(String info, List<String> actions) {

    }

    @Override
    public void quit() {

    }

}
