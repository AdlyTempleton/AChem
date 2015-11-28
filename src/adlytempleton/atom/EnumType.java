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
    //As in X/Y symbols
    private boolean flexible;

    EnumType(char symbol, Color color) {
        this.symbol = symbol;
        this.color = color;
    }

    EnumType(char symbol, Color color, boolean flexible) {
        this(symbol, color);
        this.flexible = flexible;
    }

    /**
     * Returns the EnumType that correponds to a given character
     */
    public static EnumType fromChar(char c) {

        for (EnumType type : values()) {
            if (type.symbol == c) {
                return type;
            }
        }

        return null;
    }

    public boolean isFlexible() {
        return flexible;
    }

    /**
     * Check if the types match
     * In a way useful for matching reaction rules to products
     * Checks for an exact match or wildcard
     *
     * @param type The other type
     * @return True if the two match or are accessible via wildcard
     */
    public boolean matches(EnumType type) {
        return this == type || this.isFlexible() || type.isFlexible();
    }
}
