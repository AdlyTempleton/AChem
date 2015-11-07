package adlytempleton.graphics;

import adlytempleton.atom.Atom;
import adlytempleton.map.AbstractMap;
import adlytempleton.map.SquareLocation;
import adlytempleton.map.SquareMap;

/**
 * Created by ATempleton on 11/7/2015.
 *
 * Prints a representation of the map into the terminal
 */
public class GLIGraphics {
    public static void render(SquareMap map){
        for(int x = 0; x < map.getSize(); x++){
            //Print a representation line by line
            String line = "";

            for(int y = 0; y < map.getSize(); y ++){
                Atom atom = map.getAtomAtLocation(new SquareLocation(x, y));
                line += atom == null ? "-" : atom.type.symbol;
            }

            System.out.println(line);
        }

        System.out.println("");
    }
}
