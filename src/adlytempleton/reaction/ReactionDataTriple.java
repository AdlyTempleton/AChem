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

package adlytempleton.reaction;

import adlytempleton.atom.Atom;
import adlytempleton.atom.EnumType;
import adlytempleton.map.AbstractMap;
import adlytempleton.map.Simulator;

/**
 * Created by ATempleton on 1/10/2016.
 */
public class ReactionDataTriple extends ReactionData {

    public final EnumType type3;
    public final int preState3;
    public final int postState3;

    public final boolean preBonded23;
    public final boolean preBonded31;

    public final boolean postBonded23;
    public final boolean postBonded31;


    /**
     * Constructs a reaction from all component elements
     *
     * @param type1          Type of atom 1
     * @param type2          Type of atom 2
     * @param preState1      State of atom 1 before reaction
     * @param preState2      State of atom 2 before reaction
     * @param postState1     State of atom 1 after reaction
     * @param postState2     State of atom 2 after reaction
     * @param preBonded      Are atoms 1 and 2 bonded before reaction
     * @param postBonded     Are atoms 1 and 2 bonded after reaction
     */
    public ReactionDataTriple(EnumType type1, EnumType type2, EnumType type3, int preState1, int preState2, int prestate3, int postState1, int postState2, int postState3, boolean preBonded, boolean preBonded23, boolean preBonded31, boolean postBonded, boolean postBonded23, boolean postBonded31) {
        super(type1, type2, preState1, preState2, postState1, postState2, preBonded, postBonded, false);

        this.type3 = type3;
        this.preState3 = prestate3;
        this.postState3 = postState3;
        this.preBonded23 = preBonded23;
        this.preBonded31 = preBonded31;
        this.postBonded23 = postBonded23;
        this.postBonded31 = postBonded31;
    }



    public static ReactionData fromString(String s) {

        //Preprocess for sanity
        s = s.replaceAll(" ", "");

        //Find type1
        EnumType type1 = EnumType.fromChar(s.charAt(0));
        s = s.substring(1);

        //Find preState1
        String n = ReactionData.digitSubstring(s);
        int preState1 = Integer.parseInt(n);
        s = s.replaceFirst(n, "");

        //Find preBonded
        boolean preBonded = s.startsWith("-");
        s = s.substring(1);

        //Find type2
        EnumType type2 = EnumType.fromChar(s.charAt(0));
        s = s.substring(1);

        //Find preState2
        n = ReactionData.digitSubstring(s);
        int preState2 = Integer.parseInt(n);
        s = s.replaceFirst(n, "");

        //Find preBonded
        boolean preBonded23 = s.startsWith("-");
        s = s.substring(1);

        //Find type2
        EnumType type3 = EnumType.fromChar(s.charAt(0));
        s = s.substring(1);

        //Find preState2
        n = ReactionData.digitSubstring(s);
        int preState3 = Integer.parseInt(n);
        s = s.replaceFirst(n, "");

        //Find preBonded31
        boolean preBonded31 = s.startsWith("-");
        s = s.substring(1);

        //Remove arrow
        s = s.substring(2);

        //Find postState1
        n = ReactionData.digitSubstring(s);
        int postState1 = Integer.parseInt(n);
        s = s.replaceFirst(n, "");

        //Find postBonded
        boolean postBonded = s.startsWith("-");
        s = s.substring(1);

        //Find postState2
        n = ReactionData.digitSubstring(s);
        int postState2 = Integer.parseInt(n);
        s = s.replaceFirst(n, "");

        //Find postBonded23
        boolean postBonded23 = s.startsWith("-");
        s = s.substring(1);

        //Find postState3
        n = ReactionData.digitSubstring(s);
        int postState3 = Integer.parseInt(n);
        s = s.replaceFirst(n, "");

        //Find postBonded31
        boolean postBonded31 = s.startsWith("-");
        s = s.substring(1);

        return new ReactionDataTriple(type1, type2, type3, preState1, preState2, preState3, postState1, postState2, postState3, preBonded, preBonded23, preBonded31, postBonded, postBonded23, postBonded31);
    }

    @Override
    public String toString() {
        //Forms a representative string for a reaction
        return String.format("%c%d %c %c%d %c %c%d %c to %d %c %d %c %d %c",
                type1.symbol, preState1, preBonded ? '-' : '+', type2.symbol, preState2, preBonded23 ? '-' : '+', type3.symbol, preState3, preBonded31 ? '-' : '+',
                postState1, postBonded ? '-' : '+', postState2, postBonded23 ? '-' : '+', postState3, postBonded31 ? '-' : '+');
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new ReactionDataTriple(type1, type2, type3, preState1, preState2, preState3, postState1, postState2, postState3, preBonded, preBonded23, preBonded31, postBonded, postBonded23, postBonded31);
    }

    @Override
    public int hashCode() {
        //Each integer that defines the atom is multiplied by 37 to some power
        int result = 13;
        result = result * 37 + type1.ordinal();
        result = result * 37 + type2.ordinal();
        result = result * 37 + type3.ordinal();
        result = result * 37 + preState1;
        result = result * 37 + preState2;
        result = result * 37 + preState3;
        result = result * 37 + postState1;
        result = result * 37 + postState2;
        result = result * 37 + postState3;
        result = result * 37 + (preBonded ? 0 : 1);
        result = result * 37 + (preBonded23 ? 0 : 1);
        result = result * 37 + (preBonded31 ? 0 : 1);
        result = result * 37 + (postBonded ? 0 : 1);
        result = result * 37 + (postBonded23 ? 0 : 1);
        result = result * 37 + (postBonded31 ? 0 : 1);
        return result;
    }

    @Override
    public boolean matches(Atom a1, Atom a2, Atom a3) {
        //Find all permutations
        return matchesTriple(a1, a2, a3) || matchesTriple(a1, a3, a2) || matchesTriple(a2, a1, a3) || matchesTriple(a2, a3, a1) || matchesTriple(a3, a1, a2) || matchesTriple(a3, a2, a1);
    }



    /**
     * Checks if a ReactionData is applicable to a given triple of atoms. This is order-specific
     *
     * @param a1 First Atom
     * @param a2 Second Atom
     * @return True if atom is valid
     */
    private boolean matchesTriple(Atom a1, Atom a2, Atom a3) {

        //If the reaction applies to any two atoms of the same type
        boolean sameTypes = type1.isWildcard() && type1 == type2 && type2 == type3;

        //Types of atoms are valid
        boolean typesMatch = (sameTypes && a1.type == a2.type && a2.type == a3.type) || (!sameTypes && a1.type.matches(type1) && a2.type.matches(type2) && a3.type.matches(type3));

        //States of atoms are valid
        boolean statesMatch = a1.state == preState1 && a2.state == preState2 && a3.state == preState3;

        boolean bondsMatch = (a1.isBondedTo(a2) == preBonded) && (a2.isBondedTo(a3) == preBonded23) && (a3.isBondedTo(a1) == preBonded31);

        return typesMatch && statesMatch && bondsMatch;

    }

    @Override
    public boolean apply(Atom a1, Atom a2, Atom a3, AbstractMap map, Simulator simulator) {
        if(matchesTriple(a1, a2, a3)){
            return applyTriple(a1, a2, a3, map, simulator);
        }
        if(matchesTriple(a1, a3, a2)){
            return applyTriple(a1, a3, a2, map, simulator);
        }
        if(matchesTriple(a2, a3, a1)){
            return applyTriple(a2, a3, a1, map, simulator);
        }
        if(matchesTriple(a2, a1, a3)){
            return applyTriple(a2, a1, a3, map, simulator);
        }
        if(matchesTriple(a3, a1, a2)){
            return applyTriple(a3, a1, a2, map, simulator);
        }
        if(matchesTriple(a3, a2, a1)){
            return applyTriple(a3, a2, a1, map, simulator);
        }

        return false;
    }

    /**
     * Applies the reaction to an order-dependent triple of atoms
     */
    private boolean applyTriple(Atom atom1, Atom atom2, Atom atom3, AbstractMap map, Simulator simulator) {

        //Check that all bonds formed are legal
        if((postBonded && simulator.doesBondCross(atom1, atom2)) || (postBonded23 && simulator.doesBondCross(atom2, atom3)) || (postBonded31 && simulator.doesBondCross(atom3, atom1))){
            return false;
        }
        if (ReactionManager.enzymeNearby(atom1, atom2, this, map) && ReactionManager.enzymeNearby(atom2, atom3, this, map)) {

            atom1.state = postState1;
            atom2.state = postState2;
            atom3.state = postState3;

            //The bond and unbond methods contain the checks for the pre-reaction states
            if (postBonded) {
                atom1.bond(atom2);
            } else {
                atom2.unbond(atom1);
            }

            if(postBonded23){
                atom2.bond(atom3);
            }else{
                atom2.unbond(atom3);
            }

            if(postBonded31){
                atom3.bond(atom1);
            }else{
                atom3.unbond(atom1);
            }

            return true;
        }
        return false;
    }
}

