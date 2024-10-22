package persistence;

import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;
import domain.car.options.OptionCategory;
import domain.scheduler.AlgorithmBuilder;
import domain.scheduler.FIFOBuilder;
import domain.scheduler.SpecificationBatchBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataSeeder {

    public static LinkedList<WorkStation> defaultAssemblyLine() {
        LinkedList<WorkStation> workStations = new LinkedList<>();

        List<String> actions1 = new ArrayList<>();
        actions1.add("Lift the body shell onto the chassis frame.");
        actions1.add("Bolt the shell and the frame together.");
        AssemblyTask task1 = new AssemblyTask("Assembly car body", actions1, new OptionCategory("Body"));

        List<String> actions2 = new ArrayList<>();
        actions2.add("Make sure the car is clean, remove dust  if not.");
        actions2.add("Sand the body.");
        actions2.add("Clean it.");
        actions2.add("Tape the surfaces.");
        actions2.add("Paint the car.");
        AssemblyTask task2 = new AssemblyTask("Paint car", actions2, new OptionCategory("Color"));

        List<String> actions3 = new ArrayList<>();
        actions3.add("Using the hoist, slowly lower the new motor into position.");
        actions3.add("Connect it to the transmission.");
        actions3.add("Reinstall the vacuum and fuel lines, and the throttle cables and accessories.");
        actions3.add("Reinstall the air intake manifold, battery, and fan belts.");
        actions3.add("Refill all of the fluids.");
        AssemblyTask task3 = new AssemblyTask("Insert engine", actions3, new OptionCategory("Engine"));

        List<String> actions4 = new ArrayList<>();
        actions4.add("Bolt the gearbox.");
        actions4.add("Connect the starter.");
        actions4.add("Connect the driveshaft.");
        actions4.add("Refill the transmission fluid.");
        actions4.add("Connect the exhaust.");
        actions4.add("Connect the shifter.");
        actions4.add("Connect the battery.");
        AssemblyTask task4 = new AssemblyTask("Insert gearbox", actions4, new OptionCategory("Gearbox"));

        List<String> actions5 = new ArrayList<>();
        actions5.add("Install the seat belt components.");
        actions5.add("Attach the bolts for each seat.");
        AssemblyTask task5 = new AssemblyTask("Install seats", actions5, new OptionCategory("Seats"));

        List<String> actions6 = new ArrayList<>();
        actions6.add("Install the air.");
        actions6.add("Install the co.");
        AssemblyTask task6 = new AssemblyTask("Install airco", actions6, new OptionCategory("Airco"));

        List<String> actions7 = new ArrayList<>();
        actions7.add("Lift the car.");
        actions7.add("Set the wheels on the studs.");
        actions7.add("Put the lug nuts on.");
        actions7.add("Put on the hubcap.");
        AssemblyTask task7 = new AssemblyTask("Mount wheels", actions7, new OptionCategory("Wheels"));

        List<String> actions8 = new ArrayList<>();
        actions8.add("Add spoiler.");
        AssemblyTask task8 = new AssemblyTask("Add spoiler", actions8, new OptionCategory("Spoiler"));

        WorkStation ws1 = new WorkStation("Car Body Post",
                new ArrayList<>(List.of(task1, task2)));
        WorkStation ws2 = new WorkStation("Drivetrain Post",
                new ArrayList<>(List.of(task3, task4)));
        WorkStation ws3 = new WorkStation("Accessories Post",
                new ArrayList<>(List.of(task5, task6, task7, task8)));

        workStations.add(ws1);
        workStations.add(ws2);
        workStations.add(ws3);

        return workStations;
    }

    /**
     * Representation of the available scheduling algorithms that are available in the system.
     * Based on Key-Value pair:
     * Key = Textual representation of the Algorithm
     * Value = Builder of the algorithm class so that we can construct it in a dynamic way.
     *
     * @return Map of Scheduling algorithms
     */
    public static Map<String, AlgorithmBuilder> getSchedulingAlgorithms() {
        return Map.of(
                "FIFO", new FIFOBuilder(),
                "SB", new SpecificationBatchBuilder()
        );
    }
}
