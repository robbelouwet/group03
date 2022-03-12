package app.ui.interfaces;

import domain.WorkStation;
import domain.assembly.AssemblyTask;

import java.util.List;

public interface ICarMechanicView {
    void showWorkStations(List<WorkStation> availableWorkstations);

    void showAvailableTasks(List<AssemblyTask> workStationTasks);

    void showTaskInfo(String info, List<String> actions);

    void quit();
}
