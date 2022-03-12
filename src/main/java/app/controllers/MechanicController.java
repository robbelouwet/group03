package app.controllers;

import app.ui.CarMechanicTextView;
import app.ui.GarageHolderTextView;
import domain.WorkStation;
import domain.assembly.AssemblyLine;
import domain.assembly.AssemblyTask;
import domain.car.CarModel;
import domain.car.CarOrder;
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

    public void loginToSystem() {
        List<WorkStation> availableWorkstations =  assemblyManager.getAvailableWorkStations();
        view.showWorkStations(availableWorkstations);
    }

    void selectWorkStation(WorkStation workStation);

    void selectTask(AssemblyTask assemblyTask);

    void finishedTask();





}
