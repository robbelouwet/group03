package app.controllers;

import app.ui.interfaces.IManagerView;
import services.assembly.AssemblyManager;

public class ManagerController {
    private final AssemblyManager assemblyManager = AssemblyManager.getInstance();

    private IManagerView managerView;

    public ManagerController() {
        // TODO: create controller and/or view?
    }
}
