/*
 * Copyright 2016 Adly Templeton
 *
 * This file is part of the AChem Simulator.
 *
 * The AChem Simulator is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * The AChem Simulator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Foobar. If not, see http://www.gnu.org/licenses/.
 */

package adlytempleton.gui;

import adlytempleton.map.AbstractMap;
import adlytempleton.map.SquareMap;
import adlytempleton.monitor.EnzymeMonitor;
import adlytempleton.monitor.EventTracker;
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
        setSize(1900, 1500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        squareMap = map;


        //Take up the main panel
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

        JPanel graphPanel = new JPanel();

        graphPanel.setLayout(new FlowLayout());
        graphPanel.setPreferredSize(new Dimension(400, 1200));
        graphPanel.setMinimumSize(graphPanel.getPreferredSize());
        graphPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        for (ReactionData data : EventTracker.monitoredReactions) {
            ReactionGraphPanel reactionGraph = new ReactionGraphPanel(data);
            reactionGraph.setPreferredSize(new Dimension(400, 400));

            reactionGraph.setMinimumSize(reactionGraph.getPreferredSize());
            graphPanel.add(reactionGraph);
        }
        add(graphPanel, BorderLayout.WEST);


        setVisible(true);
    }

    public void updateEnzymeList(AbstractMap map) {
        Multiset multiset = EnzymeMonitor.getNewReactions(map);
        if (enzymeList != null) {
            enzymeListModel = new DefaultListModel<>();

            enzymeListModel.addElement("New Enzymes");

            for (Object obj : multiset.elementSet()) {
                ReactionData rxn = (ReactionData) obj;



                //Every instance of a given reaction is given in the appropriate bucket
                //S
                String s = rxn.toString() + "   (" + map.enzymes.get(rxn.preState1).keys().count(rxn) + ")";
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