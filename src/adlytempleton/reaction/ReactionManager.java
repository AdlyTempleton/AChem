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
import adlytempleton.map.ILocation;
import adlytempleton.map.Simulator;
import adlytempleton.simulator.SimulatorConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ATempleton on 11/14/2015.
 */
public class ReactionManager {


    /**
     * Performs a reaction on two atoms, if it is possible. Order-independent
     *
     * @param atom1 First Atom
     * @param atom2 Second Atom
     * @param atom3 Third Atom
     *
     * @return True if a reaction took place
     */
    public static boolean react(Atom atom1, Atom atom2, Atom atom3, AbstractMap map, Simulator simulator) {

        //For testing purposes, this ReactionData is hardcoded in
        Set<ReactionData> reactions = map.enzymes.keySet();

        for (ReactionData reactionData : reactions) {
            if (reactionData.matches(atom1, atom2, atom3)) {
                reactionData.apply(atom1, atom2, atom3, map, simulator);
                return true;
            }

        }


        return false;
    }

    /**
     * Determines whether the reach of an enzyme is blocked by a membrane (defined as a bond between type-a atoms)
     *
     * @return true if the reaction is valid, false otherwise
     */
    public static boolean enzymePathBlocked(AbstractMap map, Atom enzyme, Atom reactant1, Atom reactant2) {
        HashSet<ILocation> crossedArea = map.getCrossedZone(enzyme.getLocation(), reactant1.getLocation());
        crossedArea.addAll(map.getCrossedZone(enzyme.getLocation(), reactant2.getLocation()));

        for (ILocation loc : crossedArea) {
            Atom atom = map.getAtomAtLocation(loc);
            //Check for A-type atom
            if (atom != null && atom.type == EnumType.A) {
                for (Atom bondedAtom : atom.bonds) {
                    if (bondedAtom.type == EnumType.A) {
                        //Check for uniqueness
                        if (!(atom == reactant1 || atom == reactant2)) {
                            if (map.crossed(reactant1.getLocation(), enzyme.getLocation(), atom.getLocation(), bondedAtom.getLocation(), true)) {
                                return false;
                            }
                            if (map.crossed(reactant2.getLocation(), enzyme.getLocation(), atom.getLocation(), bondedAtom.getLocation(), true)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Returns true if two atoms are connected by a string of bonds, false otherwise
     */
    public static boolean connected(Atom atom1, Atom atom2){
        ArrayList<Atom> checkedAtoms = new ArrayList<>();
        ArrayList<Atom> atomsToCheck = new ArrayList<>();

        atomsToCheck.add(atom1);

        //Basic depth-first search
        while (atomsToCheck.size() != 0){
            Atom atom = atomsToCheck.remove(atomsToCheck.size() - 1);
            checkedAtoms.add(atom);

            for(Atom bondedAtom : atom.bonds){
                if(!checkedAtoms.contains(bondedAtom) && !atomsToCheck.contains(bondedAtom)){
                    if(atom == atom2){
                        return true;
                    }

                    atomsToCheck.add(bondedAtom);
                }


            }
        }

        return false;
    }

    /**
     * Returns true if the reaction between atom1 and atom2 is blocked by a membrane, taking special rules into account
     * This is a wrapper around enzymePathBlocked
     * @return
     */
    public static boolean canEnzymeReach(AbstractMap map, Atom enzyme, Atom reactant1, Atom reactant2){
        if(!SimulatorConstants.MEMBRANE_BLOCKING){
            return true;
        }

        boolean lineBlocked = enzymePathBlocked(map, enzyme, reactant1, reactant2);

        boolean isMembrane = (reactant1.type == EnumType.A && reactant1.state == 36) || (reactant2.type == EnumType.A && reactant2.state == 36);

        boolean connected = connected(enzyme, reactant1) || connected(enzyme, reactant2);

        if(isMembrane){
            return connected;
        }else {
            return !lineBlocked || connected;
        }

    }

    /**
     * Given a reaction and a pair of two atoms
     * Determines whether an appropriate enzyme is nearby
     *
     * @param atom1    First atom in reaction
     * @param atom2    Second atom in reaction
     * @param reaction The reaction between the atoms. Note that this isn't determined exclusively by atom1 and atom2. Wildcard values are not taken into account
     * @param map      The map on which this reaction takes place
     * @return True if an appropriate enzyme is nearby
     */
    public static boolean enzymeNearby(Atom atom1, Atom atom2, ReactionData reaction, AbstractMap map) {
        //Cycle through all enzymes which contain a given reaction
        for (Atom enzyme : map.enzymes.get(reaction)) {
            //We want to check the distance to either product
            if (map.getDistance(enzyme.getLocation(), atom1.getLocation()) <= SimulatorConstants.ENZYME_RANGE ||
                    map.getDistance(enzyme.getLocation(), atom2.getLocation()) <= SimulatorConstants.ENZYME_RANGE) {
                if (canEnzymeReach(map, enzyme, atom1, atom2)) {
                    return true;
                }
            }
        }
        return false;
    }

}
