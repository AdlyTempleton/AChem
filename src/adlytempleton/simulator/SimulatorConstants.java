package adlytempleton.simulator;

/**
 * Created by ATempleton on 11/22/2015.
 */
public class SimulatorConstants {
    //The chance that an atom will attempt to move at a given simulation step
    //The actual chance of movement is lessened by 'collisions' with neighboring atoms
    public static final double MOVEMENT_CHANCE = .8;

    //Range in which an enzyme can catalyze a reaction
    public static final int ENZYME_RANGE = 10;

    //Reactions per enzyme
    public static final int ENZYME_CAPACITY = 10;

    public static final int MAP_SIZE = 50;

    //Chance of mutation each time an enzyme is copied
    public static final float MUTATION_CHANCE = .05F;

    //Whether actions of enzymes are blocked by membranes
    public static final boolean MEMBRANE_BLOCKING = false;
    //The abundance of zero-state 'food' particles
    //This is the percentage of cells which, if empty, will contain food
    //(Not including double counting)
    //The real percentage will be slightly lower in starting maps with initially defined atoms
    public static final float FOOT_ABUNDANCE = .3F;
    //The delay, in ms, between ticks
    //-1 pauses the simulation
    public static int simulationSpeed = 500;
    //The range which atoms will search to find reaction partners
    public static int REACTION_RANGE = 2;
}
