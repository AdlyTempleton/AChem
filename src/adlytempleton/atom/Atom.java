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

}
