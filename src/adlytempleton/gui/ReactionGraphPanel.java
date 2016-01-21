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

import adlytempleton.map.Simulator;
import adlytempleton.monitor.EventTracker;
import adlytempleton.reaction.ReactionData;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ATempleton on 1/20/2016.
 */
public class ReactionGraphPanel extends JPanel {

    ReactionData reactionData;

    public ReactionGraphPanel(ReactionData reactionData){
        this.reactionData = reactionData;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);


        g.clearRect(0, 0, getWidth(), getHeight());


        g.setFont(new Font("TimesRoman", Font.BOLD, 12));
        g.setColor(Color.BLACK);
        g.drawString(reactionData.toString(), getX() + 5, getY() + 5);

        int startY = 12;
        int endY = getHeight();
        int startX = getX();
        int endX = getWidth();

        int width = endX - startX;
        int height = endY - startY;

        //Ticks per period
        int periodWidth = 1000;
        int numPeriods = 1 + (Simulator.ticks / periodWidth);

        int pixelsPerPeriod = width / numPeriods;

        //Scale the graph based on the maximum value
        double maxValue = 0;

        for(int period = 0; period < numPeriods; period++){
            maxValue = Math.max(maxValue, EventTracker.eventsWithinPeriod(reactionData, period * periodWidth, (period + 1) * periodWidth));
        }

        g.drawString(maxValue + ":", getX() + 5, getY() + 20);

        int previousYLevel = 0;
        int yLevel = 0;

        for(int period = 0; period < numPeriods; period++){

            int periodCount = EventTracker.eventsWithinPeriod(reactionData, period * periodWidth, (period + 1) * periodWidth);
            previousYLevel = yLevel;
            yLevel = (int) (endY - (height * (periodCount / maxValue)));

            //Connect lines vertically
            g.drawLine(startX, previousYLevel, startX, yLevel);
            g.drawLine(startX + pixelsPerPeriod * period, yLevel, startX + pixelsPerPeriod * (period + 1), yLevel);

        }
    }
}
