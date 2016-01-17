package adlytempleton.gui;

import adlytempleton.map.AbstractMap;
import adlytempleton.map.SquareMap;
import adlytempleton.monitor.EnzymeMonitor;
import adlytempleton.reaction.ReactionData;
import adlytempleton.simulator.SimulatorConstants;
import com.google.common.collect.Multiset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ATempleton on 11/7/2015.
 * <p>
 * A basic window for rendering
 */
public class SquareMapFrame extends JFrame implements ActionListener {

    DefaultListModel<String> enzymeListModel;
    JList enzymeList;

    JLabel timingLabel;

    SquareMap squareMap;

    public SquareMapFrame(SquareMap map) {

        setTitle("Artifical Chemistry");
        setSize(1500, 1500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        squareMap = map;


        //Take up the main panel
        GridBagConstraints c = new GridBagConstraints();
        add(new SquareMapPanel(map), BorderLayout.CENTER);

        //Add list display
        enzymeListModel = new DefaultListModel<>();
        updateEnzymeList(map);
        enzymeList = new JList();
        enzymeList.setModel(enzymeListModel);
        enzymeList.setVisible(true);
        enzymeList.setSize(500, 1500);
        add(enzymeList, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        //Add buttons
        JButton[] buttons = new JButton[]{
                new JButton("Pause"),
                new JButton("Slow"),
                new JButton("Medium"),
                new JButton("Fast")};

        for (JButton button : buttons) {
            button.setSize(50, 1500);
            button.addActionListener(this);
            buttonPanel.add(button);
        }
        add(buttonPanel, BorderLayout.SOUTH);


        setVisible(true);
    }

    public void updateEnzymeList(AbstractMap map){
        Multiset multiset = EnzymeMonitor.getNewReactions(map);
        if(enzymeList != null) {
            enzymeListModel = new DefaultListModel<>();

            enzymeListModel.addElement("New Enzymes");

            for (Object obj : multiset.elementSet()) {
                ReactionData rxn = (ReactionData) obj;
                String s = rxn.toString() + "   (" + map.enzymes.keys().count(rxn) + ")";
                enzymeListModel.addElement(s);
            }

            enzymeList.setModel(enzymeListModel);
        }
    }

    @Override
    public void repaint(long time, int x, int y, int width, int height) {
        super.repaint(time, x, y, width, height);
        updateEnzymeList(squareMap);
    }

    @Override
    public void actionPerformed(ActionEvent e) {


        switch (e.getActionCommand()) {
            case "Pause":
                SimulatorConstants.simulationSpeed = -1;
                break;

            case "Slow":
                SimulatorConstants.simulationSpeed = 1000;
                break;

            case "Medium":
                SimulatorConstants.simulationSpeed = 100;
                break;
            case "Fast":
                SimulatorConstants.simulationSpeed = 0;
                break;
        }
    }
}