package app.controllers;

import app.ui.CarMechanicTextView;
import app.ui.GarageHolderTextView;
import domain.assembly.AssemblyLine;
import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;
import domain.car.CarModel;
import services.assembly.AssemblyManager;
import services.car.CarOrderManager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MechanicController {
    private final AssemblyManager assemblyManager = AssemblyManager.getInstance();
    private final CarMechanicTextView view;

    public MechanicController(CarMechanicTextView view) {
        this.view = view;
    }

    public void showMainMenu() {
        List<WorkStation> availableWorkstations =  assemblyManager.getAvailableWorkStations();
        view.showWorkStations(availableWorkstations);
    }

    public void selectWorkStation(WorkStation workStation){
        view.showAvailableTasks(workStation.getTasks());
    }

    public void selectTask(AssemblyTask assemblyTask){
        //view.showTaskInfo();
    }

    public void finishedTask(){}





}
