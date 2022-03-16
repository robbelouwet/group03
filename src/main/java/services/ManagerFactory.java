package services;

import domain.assembly.AssemblyLine;
import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ManagerFactory {
    @Setter
    /**
     * TODO:
     * Setter can only be called from test package
     */
    private static ManagerFactory instance;
    @Getter
    private AssemblyManager assemblyLineManager;
    @Getter
    private CarOrderManager carOrderManager;

    public ManagerFactory() {
        initializeData();
    }

    public void initializeData() {
        AssemblyTask task1 = new AssemblyTask("Assembly car body");
        AssemblyTask task2 = new AssemblyTask("Paint car");
        AssemblyTask task3 = new AssemblyTask("Insert engine");
        AssemblyTask task4 = new AssemblyTask("Insert gearbox");
        AssemblyTask task5 = new AssemblyTask("Install seats");
        AssemblyTask task6 = new AssemblyTask("Install airco");
        AssemblyTask task7 = new AssemblyTask("Mount wheels");

        WorkStation ws1 = new WorkStation("Car Body Post",
                new ArrayList<>(List.of(task1, task2)));
        WorkStation ws2 = new WorkStation("Drivetrain Post",
                new ArrayList<>(List.of(task3, task4)));
        WorkStation ws3 = new WorkStation("Accessories Post",
                new ArrayList<>(List.of(task5, task6, task7)));

        AssemblyLine aline = new AssemblyLine(new LinkedList<>(List.of(ws1, ws2, ws3)));

        assemblyLineManager = new AssemblyManager(aline);
        carOrderManager = new CarOrderManager();
    }

    public static ManagerFactory getInstance() {
        if (instance == null) instance = new ManagerFactory();
        return instance;
    }
}
