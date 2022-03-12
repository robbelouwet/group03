package app.ui;

import domain.scheduler.ProductionScheduler;

import java.util.Scanner;

class GarageHolderViewTest {
    public static void main(String[] args) {
        // TODO: bad idea, implement static initialize method that first checks the instance,
        //  then performs initializing logic
        ProductionScheduler.getInstance(); // init
        new GarageHolderTextView();
    }

}