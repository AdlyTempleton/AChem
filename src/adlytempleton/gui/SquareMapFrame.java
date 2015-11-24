package adlytempleton.gui;

import adlytempleton.map.SquareMap;

import javax.swing.*;

/**
 * Created by ATempleton on 11/7/2015.
 * <p>
 * A basic window for rendering
 */
public class SquareMapFrame extends JFrame {

    public SquareMapFrame(SquareMap map) {

        setTitle("Artifical Chemistry");
        setSize(1500, 1500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(new SquareMapPanel(map));
        setVisible(true);
    }
}