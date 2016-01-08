package adlytempleton.gui;

import adlytempleton.atom.Atom;
import adlytempleton.map.SquareLocation;
import adlytempleton.map.SquareMap;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ATempleton on 11/7/2015.
 */
public class SquareMapPanel extends JPanel {

    //The map from which data is rendered
    SquareMap map;

    public SquareMapPanel(SquareMap map) {
        this.map = map;
    }

    @Override
    protected void paintComponent(Graphics graphics) {

        //Basic initialization
        Graphics2D g = (Graphics2D) graphics;
        g.clearRect(0, 0, getWidth(), getHeight());

        //Width of individual grid cells
        int cellWidth = getWidth() / map.getSize();
        int cellHeight = getWidth() / map.getSize();

        //Set mouseover text
        if(getMousePosition() != null) {
            int mouseX = getMousePosition().x;
            int mouseY = getMousePosition().y;

            int mouseCellX = mouseX / cellWidth;
            int mouseCellY = mouseY / cellHeight;
            setToolTipText(String.format("(%d,%d)", mouseCellX, mouseCellY));
        }

        //Draw each atom onto the map
        for (Atom atom : map.getAllAtoms()) {

            //Cellular coordinates of the atoms
            int x = ((SquareLocation) atom.getLocation()).getX();
            int y = ((SquareLocation) atom.getLocation()).getY();

            //render each type of atom with a different color
            g.setColor(atom.type.color);
            g.fillOval(cellWidth * x, cellHeight * y, cellWidth, cellHeight);

            //Render the state of the atom
            //The text should be at the atoms position + half of the cell width/height
            g.setFont(new Font("TimesRoman", Font.BOLD, 15));
            g.setColor(Color.BLACK);

            //The offset from cornet of the cell
            int textOffsetX = (cellWidth / 2);
            int textOffsetY = (cellHeight / 2);
            g.drawString("" + atom.state, (int) x * cellWidth + textOffsetX, (int) y * cellHeight + textOffsetY);

            //Bonds
            //Find the position to render the center of the bonds
            int atomX = getCenter(x, cellWidth);
            int atomY = getCenter(y, cellHeight);

            for (Atom bondedAtom : atom.bonds) {
                //The location of the bonded atom
                SquareLocation bondedLocation = (SquareLocation) bondedAtom.getLocation();

                //
                int bondedX = getCenter(bondedLocation.getX(), cellWidth);
                int bondedY = getCenter(bondedLocation.getY(), cellHeight);

                g.drawLine(atomX, atomY, bondedX, bondedY);
            }
        }

    }

    /**
     * Helped method to calculate the center of the atom. Primarily used for rendering bonds
     *
     * @param pos       The coordinates (gridwise) of the atom
     * @param cellWidth The width of each cell. Calculated from the window size / cell size
     * @return (Graphical) Coordinates of the center of the atom
     */
    public int getCenter(int pos, int cellWidth) {
        return pos * cellWidth + (cellWidth / 2);
    }
}
