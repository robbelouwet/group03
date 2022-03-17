package app.ui.interfaces;

import domain.assembly.AssemblyTask;
import domain.assembly.WorkStation;

import java.util.List;

public interface ICarMechanicView {
    void showWorkStations(List<String> availableWorkstations);

    void showAvailableTasks(List<String> workStationTasks);

    void showTaskInfo(String info, List<String> actions);

}
