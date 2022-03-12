package app.controllers;

import app.ui.interfaces.IManagerView;
import services.assembly.AssemblyManager;

public class ManagerController {
    private final AssemblyManager assemblyManager = AssemblyManager.getInstance();
    private final IManagerView managerView;

    public ManagerController(IManagerView managerView) {
        // TODO: create controller and/or view?
        this.managerView = managerView;
    }

    public void advanceAssemblyLine(int timeSpent){
        assemblyManager.advance(timeSpent);
    }
}
