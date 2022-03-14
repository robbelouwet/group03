package app;


import app.ui.AppTextView;
import domain.scheduler.ProductionScheduler;

public class Main {
    public static void main(String[] args) {
        // usually, you put this view on a graphical canvas or something,
        // since we don't have that here, just create and forget the view
        initialize();
        new AppTextView();
    }

    private static void initialize() {
        ProductionScheduler.getInstance();
    }
}
