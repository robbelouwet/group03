package app.ui;

import app.controllers.ManagerController;
import app.ui.interfaces.IManagerView;

public class ManagerTextView implements IManagerView {
    ManagerController managerController;

    public ManagerTextView() {
        managerController = new ManagerController(this);
    }
}
