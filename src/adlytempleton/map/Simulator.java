package adlytempleton.map;

import adlytempleton.atom.Atom;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ATempleton on 11/7/2015.
 * <p>
 * Performs all the heavy logic of updating and moving atoms
 */
public class Simulator {

    //Constants of simulation

    //The chance that an atom will attempt to move at a given simulation step
    //The actual chance of movement is lessened by 'collisions' with neighboring atoms
    public final double MOVEMENT_CHANCE = .5;

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

        //Move all atoms
        for (Atom atom : map.getAllAtoms()) {
            if (rand.nextDouble() < MOVEMENT_CHANCE) {
                ArrayList<ILocation> nearbySpaces = map.getAdjacentLocations(atom.getLocation());
                ILocation newLocation = nearbySpaces.get(rand.nextInt(nearbySpaces.size()));

                //Emptyness of resulting space is checked in move function
                //Note that this means that the actial chance of movement is significantly less that MOVEMENT_CHANCE
                if (!willStretchBonds(atom, newLocation)) {
                    map.move(atom, newLocation);
                }
            }
        }

        //Re-render after components have changed
        map.render();
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

}