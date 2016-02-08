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

package adlytempleton.map;

import adlytempleton.atom.Atom;
import adlytempleton.atom.EnumType;
import adlytempleton.reaction.ReactionData;
import adlytempleton.reaction.ReactionManager;
import adlytempleton.simulator.SimulatorConstants;

import java.util.*;

/**
 * Created by ATempleton on 11/7/2015.
 * <p>
 * Performs all the heavy logic of updating and moving atoms
 */
public class Simulator {

    //Constants of simulation

    //The number of the current tick
    public static int ticks = 0;
    //Stores Locations which have been updated by a reaction or movement
    //And should be reachecked next tick
    public HashSet<ILocation> updatedLocations = new HashSet<>();
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
     *
     * @param ticks
     */
    public void tick(int ticks) {

        Simulator.ticks = ticks;

        Random rand = new Random();

        //Update all atoms which have changed
        //We want to add in more locations while this check is ongoing
        //Which wont be processed until the next tick
        HashSet<ILocation> updatedLocationsCopy = (HashSet<ILocation>) updatedLocations.clone();
        updatedLocations.clear();
        for (ILocation location : updatedLocationsCopy) {
            if (map.getAtomAtLocation(location) != null && map.getAtomAtLocation(location).state != 0) {
                reactAround(location);
            }
        }

        //Move all atoms
        List<Atom> atoms = new ArrayList<>(map.getAllAtoms());
        Collections.shuffle(atoms, rand);
        for (Atom atom : atoms) {
            ArrayList<ILocation> nearbySpaces = map.getLocationsWithinRange(atom.getLocation(), 1);

            Iterator iter = nearbySpaces.iterator();
            while (iter.hasNext()) {
                ILocation location = (ILocation) iter.next();
                if (map.getAtomAtLocation(location) != null || willStretchBonds(atom, location) || willCrossBonds(atom, location)) {
                    iter.remove();
                }
            }

            //A chance of remaining stationary
            nearbySpaces.add(atom.getLocation());


            if (nearbySpaces.size() > 0) {

                ILocation newLocation = nearbySpaces.get(rand.nextInt(nearbySpaces.size()));

                if (newLocation != atom.getLocation()) {


                    //Checks if the atom is an enzyme
                    //if it is, mark all cells for update which are now in it's range
                    //Note that this is only marking them for future use - so we call this before we move the enzyme
                    if (atom.isEnzyme()) {
                        updateReactions(atom.getLocation(), newLocation);
                    }


                    map.move(atom, newLocation);
                    if (atom.state != 0) {
                        reactAround(newLocation);
                    }

                    if (atom.type == EnumType.CAUSTIC) {
                        for (Atom nearbyAtom : map.getAdjacentAtoms(newLocation)) {
                            if (nearbyAtom.type != EnumType.A || nearbyAtom.bonds.size() < 2) {
                                //Prevents caustic agent from affecting atoms through membranes
                                if (!doesBondCross(atom, nearbyAtom)) {
                                    nearbyAtom.state = 0;
                                    nearbyAtom.unbondAll();
                                    nearbyAtom.setReactions(new ReactionData[10]);
                                }
                            }
                        }
                    }

                    //addToMembrane(atom);
                    pinchMembrane(atom);
                }
            }



        }

        //Re-render after components have changed
        map.render();
    }

    /**
     * After an atom moves, will expel it from the membrane if conditions are met
     */
    public void pinchMembrane(Atom atom) {
        if (atom.type == EnumType.A && atom.state == 36) {
            //Check bonded atoms
            if (atom.bonds.size() == 2) {
                Atom atom1 = atom.bonds.get(0);
                Atom atom2 = atom.bonds.get(1);
                if (atom1.state == 36 && atom1.type == EnumType.A &&
                        atom2.state == 36 && atom2.type == EnumType.A) {
                    //Check that atoms are adjacent
                    if (atom1.getLocation().distance(atom2.getLocation()) == 1) {
                        //Checkk that this membrane atom to be ejected is clumped
                        //This slows down the security of the pinching
                        if (atom1.getLocation().distance(atom.getLocation()) == 1 && atom2.getLocation().distance(atom.getLocation()) == 1)
                            if (!doesBondCross(atom1, atom2)) {
                                //Enzymes cannot leave the membrane
                                if (!atom.isEnzyme()) {

                                    //Stitch the membrane
                                    atom1.bond(atom2);

                                    //Unbond from other atoms
                                    atom.unbond(atom2);
                                    atom.unbond(atom1);

                                    //Become food
                                    atom.state = 0;
                                }
                            }
                    }
                }

            }
        }
    }

    /**
     * After an atom moves, will add the atom to an enzyme if applicable
     */
    public void addToMembrane(Atom atom) {

        if (atom.type == EnumType.A && atom.state == 0) {

            //A 2D array containing pairs of opposing atoms
            Atom[][] atomPairs = new Atom[][]{
                    {map.getAtomAtLocation(atom.getLocation().add(new SquareLocation(0, 1))), map.getAtomAtLocation(atom.getLocation().add(new SquareLocation(0, -1)))},
                    {map.getAtomAtLocation(atom.getLocation().add(new SquareLocation(-1, 0))), map.getAtomAtLocation(atom.getLocation().add(new SquareLocation(1, 0)))},
                    {map.getAtomAtLocation(atom.getLocation().add(new SquareLocation(-1, -1))), map.getAtomAtLocation(atom.getLocation().add(new SquareLocation(1, 1)))},
                    {map.getAtomAtLocation(atom.getLocation().add(new SquareLocation(-1, 1))), map.getAtomAtLocation(atom.getLocation().add(new SquareLocation(1, -1)))}
            };
            for (Atom[] pair : atomPairs) {
                Atom atom1 = pair[0];
                Atom atom2 = pair[1];
                if (atom1 != null && atom2 != null) {
                    if ((atom1.state == 37 || atom1.state == 36) && (atom2.state == 37 || atom2.state == 36) && atom1.type == EnumType.A && atom2.type == EnumType.A) {
                        //Check that no atom in the membrane is bonded to something else
                        if (atom1.bonds.size() == 2 && atom2.bonds.size() == 2) {
                            if (atom2.isBondedTo(atom1)) {
                                atom1.unbond(atom2);
                                atom1.bond(atom);
                                atom2.bond(atom);
                                atom.state = 36;

                            }
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

    /**
     * n
     * Checks if a movement will cross a bond
     *
     * @param atom        The atom to be moved
     * @param newLocation The location in which the atom will be moved
     * @return True if the movement is invalid
     */
    private boolean willCrossBonds(Atom atom, ILocation newLocation) {
        //Checks if the new location will result in any crossed bonds
        for (Atom bondedAtom : atom.bonds) {
            if (doesBondCross(atom, newLocation, bondedAtom, bondedAtom.getLocation())) {
                return true;
            }
        }

        if (!atom.bonds.isEmpty()) {
            for (Atom nearbyAtom : map.getAdjacentAtoms(atom.getLocation())) {
                if (!nearbyAtom.isBondedTo(atom)) {
                    for (Atom bondedAtom : nearbyAtom.bonds) {
                        if (map.crossed(atom.getLocation(), newLocation, nearbyAtom.getLocation(), bondedAtom.getLocation(), false)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }


    /**
     * Version of doesBondCross that uses the actual location of the atoms
     */
    public boolean doesBondCross(Atom atom1, Atom atom2) {
        return doesBondCross(atom1, atom1.getLocation(), atom2, atom2.getLocation());
    }

    /**
     * Determines if a (hypothetical) bond between two atoms crosses any other bonds
     * The two locations may or may not map to the actual locations of the atom
     */
    public boolean doesBondCross(Atom atom1, ILocation loc1, Atom atom2, ILocation loc2) {
        ArrayList<Atom> nearbyAtoms = getNearbyAtomsForCrossCheck(atom1, loc1);
        nearbyAtoms.addAll(getNearbyAtomsForCrossCheck(atom2, loc2));

        //Remove duplicates
        nearbyAtoms = new ArrayList<>(new HashSet<>(nearbyAtoms));

        //Remove original atoms
        nearbyAtoms.remove(atom1);
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
     * Returns a lsit of all Atoms which might cross with an atom if it were at a given location
     */
    private ArrayList<Atom> getNearbyAtomsForCrossCheck(Atom atom, ILocation loc) {
        //We want to look at all atoms adjacent to one of the components
        //Plus prevent X bonds
        ArrayList<Atom> nearbyAtoms = map.getAdjacentAtoms(loc, 1);

        //Prevent X-bonds
        for (SquareLocation xLoc : new SquareLocation[]{new SquareLocation(0, -2), new SquareLocation(-2, 0), new SquareLocation(2, 0), new SquareLocation(0, 2)}) {
            Atom xAtom = map.getAtomAtLocation(loc.add(xLoc));
            if (xAtom != null) {
                nearbyAtoms.add(xAtom);
            }
        }

        nearbyAtoms.remove(atom);

        return nearbyAtoms;
    }

    public void flood(AbstractMap map) {
        Random random = new Random();
        int centerX = random.nextInt(SimulatorConstants.MAP_SIZE);
        int centerY = random.nextInt(SimulatorConstants.MAP_SIZE);

        for (int x = centerX - SimulatorConstants.FLOOD_RANGE; x < centerX + SimulatorConstants.FLOOD_RANGE; x++) {
            for (int y = centerY - SimulatorConstants.FLOOD_RANGE; y < centerY + SimulatorConstants.FLOOD_RANGE; y++) {
                Atom atom = map.getAtomAtLocation(new SquareLocation(x, y));
                if (atom != null) {
                    atom.state = 0;
                    atom.unbondAll();
                    atom.setReactions(new ReactionData[10]);
                }
            }
        }
    }


    /**
     * Spawns food particles randomly distributed
     */
    public void populateFood(AbstractMap map) {
        ArrayList<EnumType> foodWeights = EnumType.weightedFoodMap();
        ArrayList<ILocation> cells = map.getAllLocations();

        //Pick the first 30% of the list, after shuffling
        Collections.shuffle(cells);
        Random r = new Random();

        for (int i = 0; i < cells.size() * SimulatorConstants.FOOD_ABUNDANCE; i++) {

            ILocation loc = cells.get(i);

            if (map.getAtomAtLocation(loc) == null) {

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
        if (centralAtom != null) {

            ArrayList<Atom> nearbyAtoms = map.getAdjacentAtoms(centralLocation, SimulatorConstants.REACTION_RANGE);

            for (Atom atom : nearbyAtoms) {

                //This allows two atoms to react when not surrounded by any other atoms
                if (map.getAdjacentAtoms(centralLocation, SimulatorConstants.REACTION_RANGE).isEmpty()) {
                    if (ReactionManager.react(atom, centralAtom, null, map, this)) {
                        updatedLocations.add(atom.getLocation());
                        updatedLocations.add(centralLocation);
                        return;
                    }
                } else {
                    for (Atom atom2 : nearbyAtoms) {
                        if (atom != atom2) {
                            if (atom2.getLocation().distance(atom.getLocation()) <= 2) {
                                if (ReactionManager.react(atom, atom2, centralAtom, map, this)) {
                                    //Because the state has changed, we must check atoms around to propagate reactions
                                    //We want this to take effect once per tick, to preserve locality, among other things (such as infinite recursion
                                    updatedLocations.add(atom.getLocation());
                                    updatedLocations.add(atom2.getLocation());
                                    updatedLocations.add(centralLocation);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
