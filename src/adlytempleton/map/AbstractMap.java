package adlytempleton.map;

import adlytempleton.atom.Atom;
import adlytempleton.reaction.ReactionData;
import com.google.common.collect.HashMultimap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by ATempleton on 11/7/2015.
 * <p>
 * An abstract class that represents the main map cells are placed on
 */
public abstract class AbstractMap {

    /**
     * Maps ReactionData to Atoms which catalyze that reaction
     * The null reaction is not mapped to anything
     * This does not need to be updated when an atom moves (As the link is to an atom, not a location
     * However, when a reaction data is changed, or when an atom is added or removed (by unusual means), this must be updated
     * Methods which change reaction data are responsible for maintaining this map
     * <p>
     * This is used to quickly calculate reactions
     * TODO: Performance optimizations by defining initial values for multimap parameters based on actual data
     */
    transient public HashMultimap<ReactionData, Atom> enzymes = HashMultimap.create();

    /**
     * Reforms the enzymes list
     * Used when reading from a file
     */
    public void updateAllEnzymes() {
        enzymes.clear();

        for (Atom atom : getAllAtoms()) {
            for (ReactionData rxn : atom.getReactions()) {
                if (rxn != null) {
                    enzymes.put(rxn, atom);
                }
            }
        }
    }

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
     * Returns all locations present on a grid
     *
     * @return An arraylist containing all valid ILocations
     */
    public abstract ArrayList<ILocation> getAllLocations();

    /**
     * Finds all locations in a specified distance from a given location (not including itself)
     *
     * @param location An AbstactLocation on the grid of type getLocationType()
     * @param range    Integer distance measured in whatever distance metric used in this map
     * @return An ArrayList of all locations adjacent to this location. Calculated via whatever metric is suitable for this grid type
     */
    public abstract ArrayList<ILocation> getLocationsWithinRange(ILocation location, int range);


    public ArrayList<Atom> getAdjacentAtoms(ILocation location) {
        return getAdjacentAtoms(location, 1);
    }

    /**
     * Finds all locations adjacent to a given location (not including itself) that contains an atom
     *
     * @param location An AbstactLocation on the grid of type getLocationType()
     * @return An ArrayList of all atoms adjacent to this location. Calculated via whatever metric is suitable for this grid type
     */
    public ArrayList<Atom> getAdjacentAtoms(ILocation location, int range) {

        ArrayList<ILocation> locations = getLocationsWithinRange(location, range);
        ArrayList<Atom> result = new ArrayList<Atom>();

        for (ILocation nearbyLocation : locations) {
            if (getAtomAtLocation(nearbyLocation) != null) {
                result.add(getAtomAtLocation(nearbyLocation));
            }
        }

        return result;

    }

    /**
     * Checks if four locations are crossed on the grid geometry
     * <p>
     * In strict mode, this function will return true if one of the endpoints of one line lies along the second line
     *
     * @param loc11  First atom of first bond
     * @param loc12  Second atom of first bond
     * @param loc21  First atom of second bond
     * @param loc22  Second atom of second bond
     * @param strict Determines the behavior of this function. Explained above
     * @return True if the two bonds described are crossed
     */
    public abstract boolean crossed(ILocation loc11, ILocation loc12, ILocation loc21, ILocation loc22, boolean strict);

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
     * Draw a line between loc1 and loc2
     * Collect all points on the
     * Then draw a 'rectangle' extending two points to the right and collect all points on that rectangle
     * Every bond between atoms which crosses the line between loc1 and loc2
     * Must contain one atom in this set of points
     *
     * @return
     */
    public abstract HashSet<ILocation> getCrossedZone(ILocation loc1, ILocation loc2);


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
     * For a movement (ie. of an enzyme) from start to end
     * This method returns all ILocations which are now within the range of the enzyme
     * But were not so before.
     * <p>
     * This is only valid for movements of one unit distance
     * <p>
     * The list will be empty if either the movement is zero or if the range falls outside the grid
     *
     * @param start The location before the movement
     * @param end   The location after the movement
     * @param range The range to check (Usually SimulatorConstants.ENZYME_RANGE)
     */
    public abstract List<ILocation> newlyInRange(ILocation start, ILocation end, int range);


    /**
     * This should be called whenever the ReactionData of an atom is changed. This must be called before the actual change is made
     *
     * @param atom        The atom that is being changed. Responsible for updating enzyme maps, if applicable.
     * @param newReaction The new reaction data
     */
    public abstract void updateEnzymes(Atom atom, ReactionData[] newReaction);

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
