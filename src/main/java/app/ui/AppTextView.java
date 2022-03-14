package app.ui;

import app.controllers.AppController;
import app.ui.interfaces.IAppView;

import java.util.Scanner;

public class AppTextView implements IAppView {
    private final AppController appController;

    public AppTextView() {
        appController = new AppController(this);
    }

    @Override
    public String showMenu() {
        Scanner s = new Scanner(System.in);
        // TODO: show the menu
        // is he a mechanic, manager or garage holder?
        System.out.println("Who are you?");
        return s.nextLine();
    }

    @Override
    public void start() {
        appController.initialize();
    }
}
