package app.controllers;

import app.ui.interfaces.ICarMechanicView;
import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;
import services.AssemblyManager;
import services.ManagerStore;
import services.MechanicManager;

import java.util.List;
import java.util.stream.Collectors;

public class MechanicController {
    private ICarMechanicView ui;
    private final MechanicManager mechanicManager;
    private final AssemblyManager assemblyManager;

    MechanicController(MechanicManager mechanicManager, AssemblyManager assemblyManager) {
        this.mechanicManager = mechanicManager;
        this.assemblyManager = assemblyManager;
    }

    public void setUi(ICarMechanicView ui) {
        this.ui = ui;
    }

    public void showMainMenu() {
        List<WorkStation> availableWorkstations =  assemblyManager.getBusyWorkStations();
        ui.showWorkStations(availableWorkstations.stream().map(WorkStation::getName).collect(Collectors.toList()));
    }

    public void selectWorkStation(String workStationName){
        var ws = assemblyManager.getBusyWorkStations().stream()
                .filter(w -> w.getName().equals(workStationName))
                .findAny().orElseThrow();
        mechanicManager.setCurrentWorkStation(ws);
        ui.showAvailableTasks(mechanicManager.getCurrentWorkStation().getPendingTasks().stream().map(AssemblyTask::toString).collect(Collectors.toList()));
    }

    public void selectTask(String assemblyTaskName){
        var task = mechanicManager.selectTask(assemblyTaskName);
        ui.showTaskInfo(task.getTaskInformation(), task.getActions());
    }

    public void finishTask(){
        mechanicManager.finishTask();
        ui.showAvailableTasks(mechanicManager.getTaskNames());
    }

    public boolean isTaskName(String name) {
        return mechanicManager.isValidTask(name);
    }
}
