package adlytempleton.map;

import adlytempleton.atom.Atom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ATempleton on 11/7/2015.
 * <p>
 * An abstract class that represents the main map cells are placed on
 */
public abstract class AbstractMap {

    /**
     * @return The subclass of ILocation used by this type of map
     */
    public abstract Class<? extends ILocation> getLocationType();

    /**
     * @param location An AbstactLocation object present on this grid of type getLocationType()
     * @return Atom object present at particular location
     */
    public abstract Atom getAtomAtLocation(ILocation location);


    /**
     * Finds all locations adjacent to a given location (not including itself)
     *
     * @param location An AbstactLocation on the grid of type getLocationType()
     * @return An ArrayList of all locations adjacent to this location. Calculated via whatever metric is suitable for this grid type
     */
    public abstract ArrayList<ILocation> getAdjacentLocations(ILocation location);

    /**
     * Finds all locations adjacent to a given location (not including itself) that contains an atom
     *
     * @param location An AbstactLocation on the grid of type getLocationType()
     * @return An ArrayList of all atoms adjacent to this location. Calculated via whatever metric is suitable for this grid type
     */
    public ArrayList<Atom> getAdjacentAtoms(ILocation location){

        ArrayList<ILocation> locations = getAdjacentLocations(location);
        ArrayList<Atom> result = new ArrayList<Atom>();

        for(ILocation nearbyLocation : locations){
            if(getAtomAtLocation(nearbyLocation) != null){
                result.add(getAtomAtLocation(nearbyLocation));
            }
        }

        return result;

    }

    /**
     * Checks if four locations are crossed on the grid geometry
     * @param loc11 First atom of first bond
     * @param loc12 Second atom of first bond
     * @param loc21 First atom of second bond
     * @param loc22 Second atom of second bond
     * @return True if the two bonds described are crossed
     */
    public abstract boolean crossed(ILocation loc11, ILocation loc12, ILocation loc21, ILocation loc22);

    /**
     * Moves an atom from one location to another. This should both update the location in the map and in the Atom itself.
     * If an atom already exists at this location, the movement should be canceled
     *
     * @param atom        The atom to be moved
     * @param newLocation The new location to move it to (of type getLocationType)
     * @return True if movement succeeeded, false otherwise
     */
    public abstract boolean move(Atom atom, ILocation newLocation);

    /**
     * @param location1 First AbstactLocation of type getLocationType()
     * @param location2 Second ILocation of type getLocationType()
     * @return The distance between the two locations on the grid. Calculated via whatever metric is suitable for this grid type
     */
    public abstract int getDistance(ILocation location1, ILocation location2);

    /**
     * Finds all atoms on the map
     *
     * @return A List containing all Atoms
     */
    public abstract List<Atom> getAllAtoms();


    /**
     * Checks if a given location is valid.
     * <p>
     * On a square grid, this checks if the location is within the boundaries of the grid.
     * In more complex grids, this is used to check if the combination of coordinates itself combines to a valid position
     *
     * @param location The location to check of type getLocationType()
     * @return True if the location lies within the map
     */
    public abstract boolean isOnGrid(ILocation location);


    /**
     * Renders the map on-screen in whatever method is appropriate
     */
    public abstract void render();

    /**
     * Adds an Atom to the map. Also update's the Atom's stored location information. Will fail if the space is occupied
     *
     * @param location Place to add the atom.
     * @param atom     The atom to place
     * @return True if the operation succeeded, false otherwise
     */
    public abstract boolean addAtom(ILocation location, Atom atom);


}
