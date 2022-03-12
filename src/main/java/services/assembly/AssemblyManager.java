package services.assembly;

import domain.assembly.AssemblyLine;
import lombok.Getter;

// package-private!
public class AssemblyManager {
    @Getter
    private static final AssemblyManager instance = new AssemblyManager();
    private final AssemblyLine assemblyLine = new AssemblyLine();

    public void advance(int timeSpent) {
        assemblyLine.advance(timeSpent);
    }
}
