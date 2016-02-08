/*
 * Copyright 2016 Adly Templeton
 *
 * This file is part of the AChem Simulator.
 *
 * The AChem Simulator is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * The AChem Simulator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Foobar. If not, see http://www.gnu.org/licenses/.
 */

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
    A('A', Color.YELLOW, 80),
    B('B', Color.CYAN, 80),
    C('C', Color.GREEN, 10),
    D('D', Color.LIGHT_GRAY),
    E('E', Color.ORANGE, 20),
    F('F', Color.PINK, 20),
    X('X', Color.BLACK, true),
    Y('Y', Color.BLACK, true),
    CAUSTIC('.', Color.BLACK);

    public char symbol;
    public Color color;
    //Weight in food generation
    public int weight = 0;
    //If a type can be applied to any other type
    //As in X/Y symbols
    private boolean flexible;

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

    public static EnumType[] valuesExcludingCaustic() {
        return new EnumType[]{A, B, C, D, E, F, X, Y};
    }

    /**
     * Returns a list of EnumTypes, weighted by their weight value
     * Used for food generation
     */
    public static ArrayList<EnumType> weightedFoodMap() {
        ArrayList result = new ArrayList();
        for (EnumType type : EnumType.values()) {
            for (int i = 0; i < type.weight; i++) {
                result.add(type);
            }
        }

        return result;
    }

    /**
     * Returns a list of 'standard' types
     */
    public static ArrayList<EnumType> standardTypes() {
        ArrayList result = new ArrayList();
        for (EnumType type : EnumType.values()) {
            if (!type.isWildcard()) {
                result.add(type);
            }
        }

        return result;
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

    public boolean isWildcard() {
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
        return this == type || this.isWildcard() || type.isWildcard();
    }
}
