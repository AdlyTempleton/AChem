package adlytempleton.map;

import adlytempleton.atom.Atom;
import adlytempleton.gui.SquareMapFrame;
import adlytempleton.reaction.ReactionData;
import com.google.gson.annotations.Expose;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ATempleton on 11/7/2015.
 * <p>
 * Basic implementation of ILocation on a square grid
 */
public class SquareMap extends AbstractMap {

    private int size;

    private transient SquareMapFrame renderer;

    //The main HashMap that stores locations of all atoms.
    //If an atom is not present at an location, the location should not be a key in the hashmap.
    private ConcurrentHashMap<SquareLocation, Atom> atomMap = new ConcurrentHashMap<SquareLocation, Atom>();

    public SquareMap(int size) {
        this.size = size;

        renderer = new SquareMapFrame(this);
    }

    @Override
    public boolean move(Atom atom, ILocation newLocation) {
        assert newLocation instanceof SquareLocation;


        if (!atomMap.containsKey(newLocation)) {


            //Remove the atom from the current map
            atomMap.remove(atom.getLocation());

            //Update the location in the Atom object
            atom.setLocation(newLocation);

            //Insert the atom in it's new location
            atomMap.put((SquareLocation) newLocation, atom);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Class<? extends ILocation> getLocationType() {
        return SquareLocation.class;
    }

    @Override
    public Atom getAtomAtLocation(ILocation location) {
        assert location instanceof SquareLocation;

        return atomMap.get(location);
    }

    @Override
    public ArrayList<ILocation> getLocationsWithinRange(ILocation location, int range) {

        assert location instanceof SquareLocation;

        int x = ((SquareLocation) location).getX();
        int y = ((SquareLocation) location).getY();

        ArrayList<ILocation> result = new ArrayList<ILocation>();

        for(int dx = -range; dx <= range; dx++){
            for(int dy = -range; dy <= range; dy++){
                result.add(new SquareLocation(x + dx, y + dy));
            }
        }

        //Avoid counting the atom itself
        result.remove(new SquareLocation(x, y));

        //Check that all these locations lie within the boundaries of the grid
        Iterator iter = result.iterator();
        while (iter.hasNext()) {
            if (!isOnGrid((ILocation) iter.next())) {
                iter.remove();
            }
        }

        return result;
    }

    @Override
    public boolean crossed(ILocation loc11, ILocation loc12, ILocation loc21, ILocation loc22) {
        /**
         * Two bonds are crossed if their coordinates are interwoven.
         * And they are not parallel
         */

        //Convert ILocations
        SquareLocation sq11 = (SquareLocation) loc11;
        SquareLocation sq12 = (SquareLocation) loc12;
        SquareLocation sq21 = (SquareLocation) loc21;
        SquareLocation sq22 = (SquareLocation) loc22;

        //Check if lines are parallel
        //The ternary operator is user as a zero check
        double slope1 = sq11.getX() == sq12.getX() ? Double.MAX_VALUE : (sq11.getY() - sq12.getY()) / (double)(sq11.getX() - sq12.getX());
        double slope2 = sq21.getX() == sq22.getX() ? Double.MAX_VALUE : (sq21.getY() - sq22.getY()) / (double)(sq21.getX() - sq22.getX());

        //Compute y-intersection
        double b1 = sq11.getY() - sq11.getX() * slope1;
        double b2 = sq21.getY() - sq21.getX() * slope2;

        /**
         * Derived formula:
         * y = m1x + b1 = xm2 + b2
         * m1x + b1 = m2x + b2
         * m1x - m2x = b2 - b1
         * x(m1 - m2) = b2 - b1
         x = (b2 - b1) / (m1 - m2)
         */

        //Find the x of the intersection

        //If the lines are parallel
        if(slope1 == slope2){
            //Then the lines intersect if and only if
            //the four numbers are interwoven
            //And the y-intercepts are equal
            return b1 == b2 && numbersInterwoven(sq11.getX(), sq12.getX(), sq21.getX(), sq22.getX());

        }

        double x;
        double y;
        //Check for infinite slopes
        //If we have one infinite slope, x is on the verticle line
        if(slope1 == Double.MAX_VALUE && slope2 != Double.MAX_VALUE){
            x = sq11.getX();
            y = x * slope2 + b2;
        }else if(slope2 == Double.MAX_VALUE && slope1 != Double.MAX_VALUE){
            x = sq21.getX();
            y = x * slope1 + b1;
        }else if(slope1 == Double.MAX_VALUE && slope2 == Double.MAX_VALUE){
            //These intersect if and only if x coordinates are the same
            //AND the four numbers are interwoven
            return (sq11.getX() == sq21.getX() && numbersInterwoven(sq11.getY(), sq12.getY(), sq21.getY(), sq22.getY()));
        }else{
            x = (b2 - b1) / (slope1 - slope2);
            y = x * slope1 + b1;
        }

        //Now we have the intersection point of the two lines
        //But we must determine if this intersection point is contained in both segments
        boolean withinLine1 = numbersInterwoven(sq11.getX(), sq12.getX(), x) && numbersInterwoven(sq11.getY(), sq12.getY(), y);
        boolean withinLine2 = numbersInterwoven(sq21.getX(), sq22.getX(), x) && numbersInterwoven(sq21.getY(), sq22.getY(), y);

        return (withinLine1 && withinLine2);
    }

    /**
     * Helper method to determine if four coordinates are crodded on one axis
     *
     * @param a1 First coord of first atom
     * @param a2 Second coord of first atom
     * @param b1 First coord of second atom
     * @param b2 Second coord of second atom
     */
    private boolean numbersInterwoven(int a1, int a2, int b1, int b2) {

        //Ensure that all the coords are in the proper order
        //ie. That a1 <= a2, and that b1 <= b2
        if (a1 > a2) {
            int c = a2;
            a2 = a1;
            a1 = c;
        }

        if (b1 > b2) {
            int c = b2;
            b2 = b1;
            b1 = c;
        }

        /**
         * If the atoms are not interwoven
         * The lower value of one is more than the highest value of the other, or vice-versa
         */
        return !(a2 < b1 || a1 > b2);
    }


    /**
     * Helper method to determine if four coordinates are crodded on one axis
     *
     * @param a1 First coord of first atom
     * @param a2 Second coord of first atom
     * @param b Third point
     * @return
     */
    private boolean numbersInterwoven(int a1, int a2, double b) {

        //Ensure that all the coords are in the proper order
        //ie. That a1 <= a2, and that b1 <= b2
        if (a1 > a2) {
            int c = a2;
            a2 = a1;
            a1 = c;
        }

        return a1 <= b && b <= a2;
    }


    @Override
    /**
     * Implementation of getDistance based on distance
     * For this calculation, one unit distance is defined as:
     * One square horizontally, vertically, and diagonally at a 45-degree
     * This choice is made for certain purposes - particuarily the calculation of Morre neighborhoods for bonds
     */
    public int getDistance(ILocation loc1, ILocation loc2) {

        assert loc1 instanceof SquareLocation;
        assert loc2 instanceof SquareLocation;

        int diffX = Math.abs(((SquareLocation) loc1).getX() - ((SquareLocation) loc2).getX());
        int diffY = Math.abs(((SquareLocation) loc1).getY() - ((SquareLocation) loc2).getY());

        return Math.max(diffX, diffY);
    }

    @Override
    public List<Atom> getAllAtoms() {
        return new ArrayList<>(atomMap.values());
    }

    @Override
    public boolean isOnGrid(ILocation location) {
        assert location instanceof SquareLocation;

        return ((SquareLocation) location).getX() < size && ((SquareLocation) location).getX() > -1
                && ((SquareLocation) location).getY() < size && ((SquareLocation) location).getY() > -1;
    }

    @Override
    public List<ILocation> newlyInRange(ILocation startLoc, ILocation endLoc, int range) {
        assert endLoc instanceof SquareLocation;
        assert startLoc instanceof SquareLocation;

        SquareLocation end = (SquareLocation) endLoc;
        SquareLocation start = (SquareLocation) startLoc;

        int deltaX = end.getX() - start.getX();
        int deltaY = end.getY() - start.getY();

        LinkedList<ILocation> result = new LinkedList<>();

        //We want a line of length 2*range + 1
        //This line is the edge of all cells contained within the range
        //If we shift in multiple dimensions, we simply take the union
        if (deltaX != 0) {
            //deltaX is used as the sign
            int x = end.getX() + range * deltaX;
            for (int y = end.getY() - range; y <= end.getY() + range; y++) {
                result.add(new SquareLocation(x, y));
            }
        }

        //Same calculations as above
        if (deltaY != 0) {
            int y = end.getY() + range * deltaY;
            for (int x = end.getX() - range; x <= end.getX() + range; x++) {
                result.add(new SquareLocation(x, y));
            }
        }

        //Remove the overlap
        if (deltaX != 0 && deltaY != 0) {
            result.remove(new SquareLocation(end.getX() + deltaX * range, end.getY() + deltaY * range));
        }

        //Check to make sure that all elements are contained in the list
        Iterator iter = result.iterator();
        while (iter.hasNext()) {
            if (!isOnGrid((ILocation) iter.next())) {
                iter.remove();
            }
        }

        return result;
    }

    @Override
    public ArrayList<ILocation> getAllLocations() {
        ArrayList result = new ArrayList();

        for(int x = 0; x < size; x ++){
            for(int y = 0; y < size; y ++){
                result.add(new SquareLocation(x, y));
            }
        }
        return result;
    }

    @Override
    public void updateEnzymes(Atom atom, ReactionData[] newReaction) {
        //First, take it out of all current enzyme mappings
        for (ReactionData rxn : atom.getReactions()) {
            if (rxn != null) {
                enzymes.remove(rxn, atom);
            }
        }

        //Then, add the new ones
        for (ReactionData rxn : newReaction) {
            if (rxn != null) {
                enzymes.put(rxn, atom);
            }
        }
    }

    @Override
    public boolean addAtom(ILocation location, Atom atom) {

        assert location instanceof SquareLocation;

        if (!atomMap.containsKey(location)) {
            atom.setLocation(location);
            atomMap.put((SquareLocation) location, atom);

            //Add to enzyme map
            if (atom.isEnzyme()) {
                for (ReactionData rxn : atom.getReactions()) {
                    if (rxn != null) {
                        enzymes.put(rxn, atom);
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void render() {
        renderer.repaint();
    }

    public int getSize() {
        return size;
    }
}
