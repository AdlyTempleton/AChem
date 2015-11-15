package adlytempleton.atom;

import adlytempleton.map.ILocation;

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

    public Atom(EnumType type) {
        this.type = type;
    }

    public ILocation getLocation() {
        return location;
    }

    public void setLocation(ILocation location) {
        this.location = location;
    }

    /**
     * Breaks the bond between two atoms, if they are bonded
     * @param atom The atom to unbond from
     */
    public void unbond(Atom atom){
        if(isBondedTo(atom)){
            bonds.remove(atom);
            atom.bonds.remove(this);
        }
    }

    /**
     * Bonds this atom to another atom, if they are not already bonded
     *
     * @param atom Other atom
     */
    public void bond(Atom atom){

        if(!isBondedTo(atom)) {
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
    public boolean isBondedTo(Atom otherAtom){
        return bonds.contains(otherAtom);
    }
}
