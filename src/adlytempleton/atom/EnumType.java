package adlytempleton.atom;

import java.awt.*;

/**
 * Created by ATempleton on 11/7/2015.
 * <p>
 * Represents the types of atom a-f
 * Contains some information useful for rendering
 */
public enum EnumType {
    A('A', Color.YELLOW),
    B('B', Color.CYAN),
    C('C', Color.GREEN),
    D('D', Color.LIGHT_GRAY),
    E('E', Color.ORANGE),
    F('F', Color.PINK),
    X('X', Color.BLACK, true),
    Y('Y', Color.BLACK, true);


    public char symbol;
    public Color color;

    //If a type can be applied to any other type
    //As in X/Y
    private boolean flexible;

    EnumType(char symbol, Color color) {
        this.symbol = symbol;
        this.color = color;
    }

    EnumType(char symbol, Color color, boolean flexible){
        this(symbol, color);
        this.flexible = flexible;
    }

    public boolean isFlexible() {
        return flexible;
    }

    public boolean matches(EnumType type){
        return this == type || this.isFlexible() || type.isFlexible();
    }
}
