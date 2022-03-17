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
        currentWorkStation = assemblyManager.getAvailableWorkStations().stream().filter(ws -> ws.getName().equals(workStationName)).findAny().orElseThrow();
        view.showAvailableTasks(currentWorkStation.getTasks().stream().map(AssemblyTask::toString).collect(Collectors.toList()));
    }

    public void selectTask(String assemblyTaskName){
        currentTask = currentWorkStation.getTasks().stream().filter(at -> at.getName().equals(assemblyTaskName)).findAny().orElseThrow();
        view.showTaskInfo(currentTask.getTaskInformation(), currentTask.getActions());
    }

    public void finishTask(){
        currentTask.finishTask();
        view.showAvailableTasks(currentWorkStation.getTasks().stream().map(AssemblyTask::toString).collect(Collectors.toList()));
    }


    public boolean isTaskName(String name) {
        return currentWorkStation.getTasks().stream().anyMatch(at -> at.getName().equals(name));
    }
}
