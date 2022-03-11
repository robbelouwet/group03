package app.ui;

import domain.ProductionScheduler;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class GarageHolderViewTest {
    public static void main(String[] args) {
        ProductionScheduler.getInstance(); // init
        new GarageHolderView(new Scanner(System.in));
    }

}