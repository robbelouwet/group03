package app.ui;

import app.controllers.TestPresenter;
import javax.swing.*;
import java.awt.*;

public class TestFrame extends JFrame {
    TestPresenter presenter;
    JPanel testPanel;
    JButton testBtn;
    JTextField textField;

    public TestFrame() throws HeadlessException {
        presenter = new TestPresenter(this);
        initializeNodes();
        layoutNodes();
    }

    private void initializeNodes() {
        setTitle("TestFrame");
        testPanel = new JPanel();
        testBtn = new JButton("Advance Assembly Line");
        textField = new JTextField();

        // moet voor elke JFrame gespecifieerd worden
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setVisible(true);
    }

    private void layoutNodes(){
        // parent frame
        setSize(500, 400);

        // child nodes, aka button and panel
        testPanel.setSize(400, 400);
        testPanel.add(testBtn);
        testPanel.add(textField);
        textField.setColumns(15);
        add(testPanel);
        testBtn.addActionListener(e -> {
            presenter.testEvent();
        });
    }

    public void updateSuccess(String text){
        textField.setText(text);
        System.out.println("Success!");
    }

}
