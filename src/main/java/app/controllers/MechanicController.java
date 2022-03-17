package app.controllers;

import app.ui.CarMechanicTextView;
import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;
import services.assembly.AssemblyManager;

import java.util.List;
import java.util.stream.Collectors;

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
        view.showWorkStations(availableWorkstations.stream().map(WorkStation::getName).collect(Collectors.toList()));
    }

    public void selectWorkStation(String workStationName){
        var selectedWorkStation =  assemblyManager.getAvailableWorkStations().stream().filter(ws -> ws.getName().equals(workStationName)).findAny().orElseThrow();
        currentWorkStation = selectedWorkStation;
        view.showAvailableTasks(selectedWorkStation.getTasks());
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
