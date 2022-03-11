package app.ui;

import app.controllers.ManagerController;
import app.ui.interfaces.IManagerView;

public class ManagerTextView implements IManagerView {
    ManagerController assemblyController;

    public ManagerTextView() {
        assemblyController = new ManagerController();
        assemblyController.setManagerView(this);
    }
}
