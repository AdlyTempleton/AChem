package adlytempleton.gui;

import adlytempleton.atom.Atom;
import adlytempleton.map.SquareLocation;
import adlytempleton.map.SquareMap;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ATempleton on 11/7/2015.
 */
public class SquareMapFrame extends JFrame {

        public JPanel panel;

        public SquareMapFrame(SquareMap map) {

            setTitle("Artifical Chemistry");
            setSize(500, 500);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            add(new SquareMapPanel(map));
            setVisible(true);
        }

}