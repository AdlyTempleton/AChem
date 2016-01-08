package adlytempleton.atom;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by ATempleton on 11/7/2015.
 * <p>
 * Represents the types of atom a-f
 * Contains some information useful for rendering
 */
public enum EnumType {
    A('A', Color.YELLOW, 4),
    B('B', Color.CYAN, 4),
    C('C', Color.GREEN),
    D('D', Color.LIGHT_GRAY),
    E('E', Color.ORANGE, 1),
    F('F', Color.PINK, 4),
    X('X', Color.BLACK, true),
    Y('Y', Color.BLACK, true);

    public char symbol;
    public Color color;
    //Weight in food generation
    public int weight = 0;
    //If a type can be applied to any other type
    //As in X/Y symbols
    private boolean flexible;

    /**
     * Returns a list of EnumTypes, weighted by their weight value
     * Used for food generation
     */
    public static ArrayList<EnumType> weightedFoodMap(){
        ArrayList result = new ArrayList();
        for(EnumType type : EnumType.values()){
            for(int i = 0; i < type.weight; i++){
                result.add(type);
            }
        }

        return result;
    }

    EnumType(char symbol, Color color) {
        this.symbol = symbol;
        this.color = color;
    }

    EnumType(char symbol, Color color, int weight) {
        this(symbol, color);
        this.weight = weight;
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
