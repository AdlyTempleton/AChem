package adlytempleton.map;

import adlytempleton.atom.Atom;
import adlytempleton.atom.EnumType;
import adlytempleton.reaction.ReactionManager;
import adlytempleton.simulator.SimulatorConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by ATempleton on 11/7/2015.
 * <p>
 * Performs all the heavy logic of updating and moving atoms
 */
public class Simulator {

    //Constants of simulation

    //Stores Locations which have been updated by a reaction or movement
    //And should be reachecked next tick
    public ArrayList<ILocation> updatedLocations = new ArrayList<>();
    AbstractMap map;

    /**
     * Constructs a new map
     *
     * @param map The AbstractMap used for a grid
     */
    public Simulator(AbstractMap map) {
        this.map = map;
    }

    /**
     * Main simulation method. Updates all elements of the simulation
     */
    public void tick() {

        Random rand = new Random();

        //Update all atoms which have changed
        //We want to add in more locations while this check is ongoing
        //Which wont be processed until the next tick
        ArrayList<ILocation> updatedLocationsCopy = (ArrayList<ILocation>) updatedLocations.clone();
        updatedLocations.clear();
        for (ILocation location : updatedLocationsCopy) {
            reactAround(location);
        }

        //Move all atoms
        for (Atom atom : map.getAllAtoms()) {
            if (rand.nextDouble() < SimulatorConstants.MOVEMENT_CHANCE) {
                ArrayList<ILocation> nearbySpaces = map.getLocationsWithinRange(atom.getLocation(), 1);

                Iterator iter = nearbySpaces.iterator();
                while (iter.hasNext()) {
                    ILocation location = (ILocation) iter.next();
                    if (map.getAtomAtLocation(location) != null || willStretchBonds(atom, location) || willCrossBonds(atom, location)) {
                        iter.remove();
                    }
                }

                if (nearbySpaces.size() > 0) {

                    ILocation newLocation = nearbySpaces.get(rand.nextInt(nearbySpaces.size()));

                    //Checks if the atom is an enzyme
                    //if it is, mark all cells for update which are now in it's range
                    //Note that this is only marking them for future use - so we call this before we move the enzyme
                    if (atom.isEnzyme()) {
                        updateReactions(atom.getLocation(), newLocation);
                    }

                    map.move(atom, newLocation);
                    reactAround(newLocation);
                    addToMembrane(atom);
                }
            }



        }

        //Re-render after components have changed
        map.render();
    }

    /**
     * After an atom moves, will add the atom to an enzyme if applicable
     */
    public void addToMembrane(Atom atom){

        Random random = new Random();

        if(atom.type == EnumType.A && atom.state == 0){
            Atom atomUp = map.getAtomAtLocation(atom.getLocation().add(new SquareLocation(0, 1)));
            Atom atomDown = map.getAtomAtLocation(atom.getLocation().add(new SquareLocation(0, -1)));
            Atom atomLeft = map.getAtomAtLocation(atom.getLocation().add(new SquareLocation(-1, 0)));
            Atom atomRight = map.getAtomAtLocation(atom.getLocation().add(new SquareLocation(1, 0)));

            if(atomUp != null && atomDown != null){
                if(atomUp.state == 36 && atomDown.state == 36 && atomUp.type == EnumType.A && atomDown.type == EnumType.A){
                    //Check that no atom in the membrane is bonded to something else
                    if(atomUp.bonds.size() == 2 && atomDown.bonds.size() == 2){
                        if(atomUp.isBondedTo(atomDown)) {
                            if (random.nextFloat() < .000000005F) {
                                atomUp.unbond(atomDown);
                                atomUp.bond(atom);
                                atomDown.bond(atom);
                                atom.state = 36;
                            }
                        }
                    }
                }
            }

            if(atomLeft != null && atomRight != null){
                if(atomLeft.state == 36 && atomLeft.state == 36 && atomLeft.type == EnumType.A && atomRight.type == EnumType.A){
                    if(atomLeft.isBondedTo(atomRight)){
                        if(random.nextFloat() < .5F) {
                            atomLeft.unbond(atomRight);
                            atomLeft.bond(atom);
                            atomRight.bond(atom);
                            atom.state = 36;
                        }
                    }
                }
            }
        }
    }

    /**
     * Uses the result of AbstractMap.newlyInRange
     * To perform all potential reactions
     * On the next tick. Uses updatedLocations as a queue
     */
    public void updateReactions(ILocation start, ILocation end) {
        updatedLocations.addAll(map.newlyInRange(start, end, SimulatorConstants.ENZYME_RANGE));
    }

    /**
     * Checks if a movement would stretch bonds beyond capacity
     *
     * @param atom        The atom to be moved
     * @param newLocation The location to which the atom will be moved
     * @return True if a movement is invalid, false otherwise
     */
    private boolean willStretchBonds(Atom atom, ILocation newLocation) {
        for (Atom bondedAtom : atom.bonds) {
            if (map.getDistance(newLocation, bondedAtom.getLocation()) > 2) {
                return true;
            }
        }
        return false;
    }

    /**n
     * Checks if a movement will cross a bond
     *
     * @param atom        The atom to be moved
     * @param newLocation The location in which the atom will be moved
     * @return True if the movement is invalid
     */
    private boolean willCrossBonds(Atom atom, ILocation newLocation) {
        for (Atom bondedAtom : atom.bonds) {
            if (doesBondCross(atom, newLocation, bondedAtom, bondedAtom.getLocation())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if a bond between two atoms crosses any other bonds
     * The two locations may or may not map to the actual locations of the atom
     */
    public boolean doesBondCross(Atom atom1, ILocation loc1, Atom atom2, ILocation loc2) {
        //We want to look at all atoms adjacent to one of the components
        //Plus prevent X bonds
        ArrayList<Atom> nearbyAtoms = map.getAdjacentAtoms(loc1, 1);
        nearbyAtoms.addAll(map.getAdjacentAtoms(loc2, 1));

        //Prevent X-bonds
        for(SquareLocation loc : new SquareLocation[]{new SquareLocation(0, -2), new SquareLocation(-2, 0), new SquareLocation(2, 0), new SquareLocation(0, 2)}) {
            Atom atom = map.getAtomAtLocation(loc1.add(loc));
            if(atom != null){
                nearbyAtoms.add(atom);
            }
        }

        //We remove the atoms twice as we are searching around the new location, not the current one
        nearbyAtoms.remove(atom1);
        nearbyAtoms.remove(atom1);
        nearbyAtoms.remove(atom2);
        nearbyAtoms.remove(atom2);

        //Cycle through all atoms bonded to these
        //This is inefficient by a factor of two
        //But this shouldn't be a performance intensive step
        for (Atom nearbyAtom : nearbyAtoms) {
            for (Atom nearbyBondedAtom : nearbyAtom.bonds) {
                if (nearbyBondedAtom != atom1 && nearbyBondedAtom != atom2 && map.crossed(loc1, loc2, nearbyAtom.getLocation(), nearbyBondedAtom.getLocation(), true)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Spawns food particles randomly distributed
     */
    public void populateFood(AbstractMap map){
        ArrayList<EnumType> foodWeights = EnumType.weightedFoodMap();
        ArrayList<ILocation> cells = map.getAllLocations();

        //Pick the first 30% of the list, after shuffling
        Collections.shuffle(cells);
        Random r = new Random();

        for(int i = 0; i < cells.size() * .1F; i ++){

            ILocation loc = cells.get(i);

            if(map.getAtomAtLocation(loc) == null){

                //Pick a random state
                EnumType state = foodWeights.get(r.nextInt(foodWeights.size()));
                Atom atom = new Atom(state, 0);
                map.addAtom(loc, atom);
            }

        }

    }


    /**
     * Checks all atoms adjacent to the given location
     * For reactions
     *
     * @param centralLocation Location of the main atom
     */
    public void reactAround(ILocation centralLocation) {
        Atom centralAtom = map.getAtomAtLocation(centralLocation);

        for (ILocation location : map.getLocationsWithinRange(centralLocation, SimulatorConstants.REACTION_RANGE)) {
            Atom atom = map.getAtomAtLocation(location);

            if (atom != null && centralAtom != null) {
                if (ReactionManager.react(atom, centralAtom, map, this)) {
                    //Because the state has changed, we must check atoms around to propagate reactions
                    //We want this to take effect once per tick, to preserve locality, among other things (such as infinite recursion
                    updatedLocations.add(location);
                    updatedLocations.add(centralLocation);
                }
            }
        }
    }

}