package adlytempleton.simulator;

/**
 * Created by ATempleton on 11/22/2015.
 */
public class SimulatorConstants {
    //The chance that an atom will attempt to move at a given simulation step
    //The actual chance of movement is lessened by 'collisions' with neighboring atoms
    public static final double MOVEMENT_CHANCE = .5;

    //Range in which an enzyme can catalyze a reaction
    public static final int ENZYME_RANGE = 5;
}
