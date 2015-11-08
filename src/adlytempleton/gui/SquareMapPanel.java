package adlytempleton.gui;

import adlytempleton.atom.Atom;
import adlytempleton.map.SquareLocation;
import adlytempleton.map.SquareMap;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ATempleton on 11/7/2015.
 */
public class SquareMapPanel extends JPanel{

    public SquareMapPanel(SquareMap map){
        this.map = map;
    }

    SquareMap map;

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.clearRect(0, 0, getWidth(), getHeight());

        //Width of individual grid cells
        int cellWidth = getWidth() / map.getSize();
        int cellHeight = getWidth() / map.getSize();

        for(Atom atom : map.getAllAtoms()){
            int x = ((SquareLocation)atom.getLocation()).getX();
            int y = ((SquareLocation)atom.getLocation()).getY();

            g.setColor(atom.type.color);
            g.fillOval(cellWidth * x, cellHeight * y, cellWidth, cellHeight);

            //Render the state of the atom
            //The text should be at the atoms position + half of the cell width/height
            g.setFont(new Font("TimesRoman", Font.BOLD, 30));
            g.setColor(Color.BLACK);

            //The offset from cornet of the cell
            int textOffsetX = (cellWidth / 2);
            int textOffsetY = (cellHeight / 2);
            g.drawString("" + atom.state, (int) x* cellWidth + textOffsetX, (int) y * cellHeight + textOffsetY);
        }

    }
}
