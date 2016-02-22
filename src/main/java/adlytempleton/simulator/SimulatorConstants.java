/*
 * Copyright 2016 Adly Templeton
 *
 * This file is part of the AChem Simulator.
 *
 * The AChem Simulator is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * The AChem Simulator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Foobar. If not, see http://www.gnu.org/licenses/.
 */

package adlytempleton.simulator;

/**
 * Created by ATempleton on 11/22/2015.
 */
public class SimulatorConstants {
    //The chance that an atom will attempt to move at a given simulation step
    //The actual chance of movement is lessened by 'collisions' with neighboring atoms
    public static double MOVEMENT_CHANCE = .8;

    //Range in which an enzyme can catalyze a reaction
    public static int ENZYME_RANGE = 10;

    //Reactions per enzyme
    public static int ENZYME_CAPACITY = 15;

    public static int MAP_SIZE = 150;

    //The highest state a reaction can reach
    public static int MAX_STATE = 256;

    //Chance of mutation each time an enzyme is copied
    public static float MUTATION_CHANCE = .1F;

    public static boolean TOROIDAL_MAP = false;

    //Whether actions of enzymes are blocked by membranes
    public static boolean MEMBRANE_BLOCKING = true;
    //The abundance of zero-state 'food' particles
    //This is the percentage of cells which, if empty, will contain food
    //(Not including double counting)
    //The real percentage will be slightly lower in starting maps with initially defined atoms
    public static float FOOD_ABUNDANCE = .2F;
    //The delay, in ms, between ticks
    //-1 pauses the simulation
    public static int simulationSpeed = 500;
    //The range which atoms will search to find reaction partners
    public static int REACTION_RANGE = 2;


    //Ticks before first floor
    public static int FLOOD_DELAY = 20000;

    //Frequency of flooding
    public static int FLOOD_FREQUENCY = 10000;

    //Radius of area affected by a floor
    public static int FLOOD_RANGE = 25;
}
