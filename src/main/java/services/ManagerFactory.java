package services;

import domain.assembly.AssemblyLine;
import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;
import lombok.Getter;
import services.assembly.AssemblyManager;
import services.car.CarOrderManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ManagerFactory {
    @Getter
    private static final ManagerFactory instance = new ManagerFactory();
    @Getter
    private AssemblyManager assemblyLineManager;
    @Getter
    private CarOrderManager carOrderManager;

    public ManagerFactory() {
        initializeData();
    }

    private void initializeData() {
        assemblyLineManager = AssemblyManager.getInstance();
        carOrderManager = new CarOrderManager();
    }
}
