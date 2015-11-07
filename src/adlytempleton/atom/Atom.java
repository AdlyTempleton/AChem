package adlytempleton.atom;

import adlytempleton.map.ILocation;

/**
 * Created by ATempleton on 11/7/2015.
 *
 * A representation of a single atom at a particular location
 */
public class Atom {

    public Atom(EnumType type){
        this.type = type;
    }
    private ILocation location;

    public EnumType type;
    public int state = 0;

    public ILocation getLocation() {
        return location;
    }

    public void setLocation(ILocation location) {
        this.location = location;
    }
}
