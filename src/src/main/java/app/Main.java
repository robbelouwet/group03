package app;

import app.ui.TestFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TestFrame();
        });
    }
}
