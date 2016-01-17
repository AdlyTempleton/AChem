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

import adlytempleton.map.AbstractMap;
import adlytempleton.map.ILocation;
import adlytempleton.reaction.ReactionData;
import adlytempleton.simulator.SimulatorConstants;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by ATempleton on 11/7/2015.
 * <p>
 * A representation of a single atom at a particular location
 */
public class Atom {


    //Type and state of the atom
    public EnumType type;
    public int state = 0;
    //Bonded atoms
    public transient ArrayList<Atom> bonds = new ArrayList<Atom>();

    /**
     * This is used when loading from json
     * To store the locations of all bonded atoms
     * This eliminates circular refrences
     * And allows the state to be saved and loaded
     * <p>
     * This contains relative locations, which should be reconstructed with ILocation.add
     */
    public ArrayList<ILocation> bondsLocation = new ArrayList<>();
    //The location of the atom
    private ILocation location;
    //Stores a fixed-length array of all Reactions this atom acts as an enzyme for
    //
    private ReactionData[] reactions = new ReactionData[SimulatorConstants.ENZYME_CAPACITY];


    public Atom(EnumType type) {
        this.type = type;
    }

    public Atom(EnumType type, int state) {
        this.type = type;
        this.state = state;
        this.reactions = new ReactionData[SimulatorConstants.ENZYME_CAPACITY];
    }

    public Atom(EnumType type, int state, ReactionData[] rxn) {
        this.type = type;
        this.state = state;
        this.reactions = rxn;
    }

    /**
     * Constructs bonds from bondsLocation
     * Used when loading from json
     */
    public void reconstructBondList(AbstractMap map) {
        bonds = new ArrayList<>();
        for (ILocation location : bondsLocation) {
            bonds.add(map.getAtomAtLocation(getLocation().add(location)));
        }
    }

    /**
     * Constructs bondsLocation data from bonds
     * Used when saving to json
     */
    public void updateBondLocationList() {
        bondsLocation = new ArrayList<>();
        for (Atom bondedAtom : bonds) {
            bondsLocation.add(bondedAtom.getLocation().subtract(getLocation()));
        }
    }

    public ILocation getLocation() {
        return location;
    }

    public void setLocation(ILocation location) {
        this.location = location;
    }

    /**
     * Breaks the bond between two atoms, if they are bonded
     *
     * @param atom The atom to unbond from
     */
    public void unbond(Atom atom) {
        if (isBondedTo(atom)) {
            bonds.remove(atom);
            atom.bonds.remove(this);
        }
    }

    @Override
    public int hashCode() {
        return location.hashCode() * state;
    }

    /**
     * Bonds this atom to another atom, if they are not already bonded
     *
     * @param atom Other atom
     */
    public void bond(Atom atom) {

        if (!isBondedTo(atom)) {
            bonds.add(atom);
            atom.bonds.add(this);
        }
    }

    /**
     * Checks if this Atom is bonded to a specific other atom
     *
     * @param otherAtom The other atom to check
     * @return True if the atoms are bonded
     */
    public boolean isBondedTo(Atom otherAtom) {
        return bonds.contains(otherAtom);
    }

    /**
     * @return A shallow clone of reactions. The shallowness of the clone is safe, as ReactionData is final.
     */
    public ReactionData[] getReactions() {
        return reactions.clone();
    }

    public void setReactions(ReactionData[] reactions) {
        this.reactions = reactions;
    }

    /**
     * Breaks all bonds this atom is connected to
     * Will not unbond caustic agents
     */
    public void unbondAll(){
        if(type != EnumType.CAUSTIC) {
            //Prevent concurrent modification exceptions
            ArrayList<Atom> bondsCopy = (ArrayList<Atom>) bonds.clone();

            for (Atom atom : bondsCopy) {
                unbond(atom);
            }
        }
    }

    /**
     * Determines whether this atom is an enzyme
     * ie. Whether getReactions() contains any non-null elements
     */
    public boolean isEnzyme() {
        for (ReactionData rxn : reactions) {
            if (rxn != null) {
                return true;
            }
        }
        return false;
    }
}
