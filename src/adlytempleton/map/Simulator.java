package adlytempleton.map;

import adlytempleton.atom.Atom;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ATempleton on 11/7/2015.
 *
 * Performs all the heavy logic of updating and moving atoms
 */
public class Simulator {

    public final double MOVEMENT_CHANCE = .5;

    AbstractMap map;

    /**
     * Constructs a new map
     *
     * @param map The AbstractMap used for a grid
     */
    public Simulator(AbstractMap map){
        this.map = map;
    }

    /**
     * Main simulation method. Updates all elements of the simulation
     */
    public void tick(){

        Random rand = new Random();

        //Move all atoms
        for(Atom atom : map.getAllAtoms()){
            if(rand.nextDouble() < MOVEMENT_CHANCE){

                ArrayList<ILocation> nearbySpaces = map.getAdjacentLocations(atom.getLocation());
                ILocation newLocation = nearbySpaces.get(rand.nextInt(nearbySpaces.size()));
                //Emptyness of resulting space is checked in move function
                //Note that this means that the actial chance of movement is significantly less that MOVEMENT_CHANCE
                map.move(atom, newLocation);
            }
        }
    }
}