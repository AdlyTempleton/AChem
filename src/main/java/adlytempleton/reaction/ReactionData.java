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
import adlytempleton.monitor.EventTracker;
import adlytempleton.mutation.MutationManager;

/**
 * Created by ATempleton on 11/14/2015.
 */
public class ReactionData {

    public final EnumType type1;
    public final EnumType type2;

    public final int preState1;
    public final int preState2;
    public final int postState1;
    public final int postState2;

    public final boolean preBonded;
    public final boolean postBonded;

    public final boolean copiesReaction;

    /**
     * Constructs a reaction from all component elements
     *
     * @param type1      Type of atom 1
     * @param type2      Type of atom 2
     * @param preState1  State of atom 1 before reaction
     * @param preState2  State of atom 2 before reaction
     * @param postState1 State of atom 1 after reaction
     * @param postState2 State of atom 2 after reaction
     * @param preBonded  Are atoms bonded before reaction
     * @param postBonded Are atoms bonded after reaction
     */
    public ReactionData(EnumType type1, EnumType type2, int preState1, int preState2, int postState1, int postState2, boolean preBonded, boolean postBonded, boolean copiesReaction) {
        this.type1 = type1;
        this.type2 = type2;
        this.preState1 = preState1;
        this.preState2 = preState2;
        this.postState1 = postState1;
        this.postState2 = postState2;
        this.preBonded = preBonded;
        this.postBonded = postBonded;
        this.copiesReaction = copiesReaction;
    }

    public static ReactionData fromString(String s) {
        if (s.startsWith("3:"))  {
            return ReactionDataTriple.fromString(s.replaceFirst("3:", ""));
        }

        //Preprocess for sanity
        s = s.replaceAll(" ", "");

        //Find type1
        EnumType type1 = EnumType.fromChar(s.charAt(0));
        s = s.substring(1);

        //Find preState1
        String n = digitSubstring(s);
        int preState1 = Integer.parseInt(n);
        s = s.replaceFirst(n, "");

        //Find preBonded
        boolean preBonded = s.startsWith("-");
        s = s.substring(1);

        //Find type2
        EnumType type2 = EnumType.fromChar(s.charAt(0));
        s = s.substring(1);

        //Find preState2
        n = digitSubstring(s);
        int preState2 = Integer.parseInt(n);
        s = s.replaceFirst(n, "");

        //Remove arrow
        s = s.substring(2);

        //Find postState1
        n = digitSubstring(s);
        int postState1 = Integer.parseInt(n);
        s = s.replaceFirst(n, "");

        //Find postBonded
        boolean postBonded = s.startsWith("-");
        s = s.substring(1);

        //Find postState2
        n = digitSubstring(s);
        int postState2 = Integer.parseInt(n);
        s = s.replaceFirst(n, "");

        boolean copiesReaction = s.equals("(cpy)");

        return new ReactionData(type1, type2, preState1, preState2, postState1, postState2, preBonded, postBonded, copiesReaction);

    }

    /**
     * Helper method for fromString
     * Starting from the beginning of the string, this method will return the initial string of digits
     */
    protected static String digitSubstring(String s) {
        String result = "";

        while (s.length() > 0 && Character.isDigit(s.charAt(0))) {
            result = result + s.charAt(0);
            s = s.substring(1);
        }

        return result;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new ReactionData(type1, type2, preState1, preState2, postState1, postState2, preBonded, postBonded, copiesReaction);
    }

    @Override
    public String toString() {
        //Forms a representative string for a reaction
        return String.format("%c%d %c %c%d to %d %c %d %s", type1.symbol, preState1, preBonded ? '-' : '+', type2.symbol, preState2, postState1, postBonded ? '-' : '+', postState2, copiesReaction ? "(cpy)" : "");
    }

    /**
     * This implementation is based around use in a HashMap
     * Does not include wildcard matching
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ReactionData)) {
            return false;
        }

        //Simple check on the representative string
        ReactionData rxn = (ReactionData) obj;
        return toString().equals(rxn.toString());
    }

    @Override
    public int hashCode() {
        //Each integer that defines the atom is multiplied by 37 to some power
        int result = 13;
        result = result * 37 + type1.ordinal();
        result = result * 37 + type2.ordinal();
        result = result * 37 + preState1;
        result = result * 37 + preState2;
        result = result * 37 + postState1;
        result = result * 37 + postState2;
        result = result * 37 + (preBonded ? 0 : 1);
        result = result * 37 + (postBonded ? 0 : 1);
        return result;
    }

    /**
     * Checks if a ReactionData is applicable to a given pair of atoms. This is order-independent
     * If the reaction only takes two components (is not ReactionDataTriple), all combinations are checked
     */
    public boolean matches(Atom a1, Atom a2, Atom a3) {
        return matches(a1, a2) || matches(a2, a3) || matches(a1, a3);
    }

    /**
     * Checks if a ReactionData is applicable to a given pair of atoms. This is order-independent
     *
     * @param a1 One Atom
     * @param a2 Another Atom
     * @return True if this reaction describes a reaction between those atoms
     */
    private boolean matches(Atom a1, Atom a2) {
        return matchesPair(a1, a2) || matchesPair(a2, a1);
    }

    /**
     * Checks if a ReactionData is applicable to a given pair of atoms. This is order-specific
     *
     * @param a1 First Atom
     * @param a2 Second Atom
     * @return True if atom is valid
     */
    private boolean matchesPair(Atom a1, Atom a2) {

        if (a1 != a2) {


            //States of atoms are valid
            if (a1.state == preState1 && a2.state == preState2) {

                if (a1.bonds.contains(a2) == preBonded) {

                    //If the reaction applies to any two atoms of the same type
                    boolean sameTypes = type1.isWildcard() && type1 == type2;

                    //Types of atoms are valid
                    if ((sameTypes && a1.type == a2.type) || (!sameTypes && a1.type.matches(type1) && a2.type.matches(type2))) {


                        return true;
                    }
                }
            }
        }

        return false;

    }

    /**
     * Runs the reaction on the given set of atoms. If this is a 2-reactant reaction, all valid reactions will be performed sequentially
     */
    public void apply(Atom a1, Atom a2, Atom a3, AbstractMap map, Simulator simulator) {


        if (matches(a1, a2)) {
            apply(a1, a2, map, simulator);
        }
        if (matches(a2, a3)) {
            apply(a2, a3, map, simulator);
        }
        if (matches(a3, a1)) {
            apply(a3, a1, map, simulator);
        }
    }

    /**
     * Runs the reaction on a given pair of atoms. Order-independent.
     */
    private boolean apply(Atom a1, Atom a2, AbstractMap map, Simulator simulator) {
        if (matchesPair(a1, a2)) {
            return applyPair(a1, a2, map, simulator);
        } else if (matchesPair(a2, a1)) {
            return applyPair(a2, a1, map, simulator);
        }
        return false;
    }

    /**
     * Runs the reaction on a given pair of atoms. Order-dependent.
     */
    private boolean applyPair(Atom atom1, Atom atom2, AbstractMap map, Simulator simulator) {

        if (!simulator.doesBondCross(atom1, atom1.getLocation(), atom2, atom2.getLocation())) {
            if (ReactionManager.enzymeNearby(atom1, atom2, this, map)) {
                atom1.state = postState1;
                atom2.state = postState2;

                //The bond and unbond methods contain the checks for the pre-reaction states
                if (postBonded) {
                    atom1.bond(atom2);
                } else {
                    atom2.unbond(atom1);
                }

                //Copies over reaction data
                if (copiesReaction) {
                    //Note that getReactions returns a shallow clone
                    map.removeFromEnzymeMap(atom2);

                    atom2.setReactions(atom1.getReactions());

                    MutationManager.mutate(atom2, map);
                    map.addToEnzymeMap(atom2);

                }

                EventTracker.notifyOfReaction(this);

                return true;
            }
        }

        return false;
    }

}
