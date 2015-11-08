package adlytempleton.atom;

import java.awt.*;

/**
 * Created by ATempleton on 11/7/2015.
 *
 * Represents the types of atom a-f
 * Contains some information useful for rendering
 */
public enum EnumType {
    A('A', Color.YELLOW), B('B', Color.CYAN), C('C', Color.GREEN), D('D', Color.LIGHT_GRAY), E('E', Color.ORANGE), F('F', Color.PINK);


    public char symbol;
    public Color color;

    EnumType(char symbol, Color color){
        this.symbol = symbol;
        this.color = color;
    }
}
