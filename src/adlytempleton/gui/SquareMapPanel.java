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
            int textOffsetX = (cellWidth / 2) - 10;
            int textOffsetY = (cellHeight / 2) - 10;
            g.drawString("" + atom.state, (int) x* cellWidth + textOffsetX, (int) y * cellHeight + textOffsetY);

            //Bonds
            int atomX = getCenterX(x, cellWidth);
            int atomY = getCenterY(y, cellHeight);

            for(Atom bondedAtom : atom.bonds){
                SquareLocation bondedLocation = (SquareLocation) bondedAtom.getLocation();

                int bondedX = getCenterX(bondedLocation.getX(), cellWidth);
                int bondedY = getCenterY(bondedLocation.getY(), cellHeight);

                g.drawLine(atomX, atomY, bondedX, bondedY);
            }
        }

    }

    public int getCenterX(int x, int cellWidth){
        return x * cellWidth + (cellWidth / 2);
    }

    public int getCenterY(int y, int cellHeight){
        return y * cellHeight + (cellHeight / 2);
    }
}
