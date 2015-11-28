package adlytempleton.atom;

import adlytempleton.map.ILocation;
import adlytempleton.reaction.ReactionData;
import adlytempleton.simulator.SimulatorConstants;

import java.util.ArrayList;

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
    public ArrayList<Atom> bonds = new ArrayList<Atom>();
    //The location of the atom
    private ILocation location;

    //Stores a fixed-length array of all Reactions this atom acts as an enzyme for
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

    public ReactionData[] getReactions() {
        return reactions;
    }

    public void setReactions(ReactionData[] reactions) {
        this.reactions = reactions;
    }

    /**
     * Determines whether this atom is an enzyme
     * ie. Whether getReactions() contains any non-null elements
     */
    public boolean isEnzyme(){
        for(ReactionData rxn : reactions){
            if(rxn != null){
                return true;
            }
        }
        return false;
    }
}
