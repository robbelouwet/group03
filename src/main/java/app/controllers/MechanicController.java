package app.controllers;

import app.ui.CarMechanicTextView;
import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;
import services.assembly.AssemblyManager;

import java.util.List;

public class MechanicController {
    private final AssemblyManager assemblyManager = AssemblyManager.getInstance();
    private final CarMechanicTextView view;
    private WorkStation currentWorkStation;
    private AssemblyTask currentTask;

    public MechanicController(CarMechanicTextView view) {
        this.view = view;
    }

    public void showMainMenu() {
        List<WorkStation> availableWorkstations =  assemblyManager.getAvailableWorkStations();
        view.showWorkStations(availableWorkstations);
    }

    public void selectWorkStation(WorkStation workStation){
        currentWorkStation = workStation;
        view.showAvailableTasks(workStation.getTasks());
    }

    public void selectTask(AssemblyTask assemblyTask){
        currentTask = assemblyTask;
        view.showTaskInfo(assemblyTask.getTaskInformation(), assemblyTask.getActions());
    }

    public void finishTask(){
        currentTask.finishTask();
        view.showAvailableTasks(currentWorkStation.getTasks());
    }





}
