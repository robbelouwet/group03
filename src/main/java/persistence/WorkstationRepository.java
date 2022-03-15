package persistence;

import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorkstationRepository extends Repository<UUID, WorkStation> implements IWorkstationRepository {
    public List<WorkStation> getWorkstations(){
        List<WorkStation> workStations = new ArrayList<>();

        // TODO: mogen de assemblyTasks aangemaakt worden in deze repo?
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

        workStations.add(ws1);
        workStations.add(ws2);
        workStations.add(ws3);

        return workStations;
    }
}
