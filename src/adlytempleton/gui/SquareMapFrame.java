package adlytempleton.gui;

import adlytempleton.map.SquareMap;
import adlytempleton.simulator.SimulatorConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ATempleton on 11/7/2015.
 * <p>
 * A basic window for rendering
 */
public class SquareMapFrame extends JFrame implements ActionListener{

    public SquareMapFrame(SquareMap map) {

        setTitle("Artifical Chemistry");
        setSize(1500, 1500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new GridBagLayout());


        //Take up the main panel
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;

        add(new SquareMapPanel(map), c);

        //Add buttons
        JButton[] buttons = new JButton[]{
                new JButton("Pause"),
                new JButton("Slow"),
                new JButton("Medium"),
                new JButton("Fast")};

        for(JButton button : buttons) {
            button.setSize(50, 1500);
            button.addActionListener(this);

            c = new GridBagConstraints();
            c.weightx = .1;
            c.fill = GridBagConstraints.HORIZONTAL;
            add(button, c);
        }

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {


        switch(e.getActionCommand()){
            case "Pause":
                SimulatorConstants.simulationSpeed = -1;
                break;

            case "Slow":
                SimulatorConstants.simulationSpeed = 500;
                break;

            case "Medium":
                SimulatorConstants.simulationSpeed = 10;
                break;
            case "Fast":
                SimulatorConstants.simulationSpeed = 0;
                break;
        }
    }
}