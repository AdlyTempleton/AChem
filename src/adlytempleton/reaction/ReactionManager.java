package adlytempleton.reaction;

import adlytempleton.atom.Atom;
import adlytempleton.atom.EnumType;
import adlytempleton.map.AbstractMap;
import adlytempleton.map.ILocation;
import adlytempleton.map.Simulator;
import adlytempleton.simulator.SimulatorConstants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ATempleton on 11/14/2015.
 */
public class ReactionManager {


    /**
     * Performs a reaction on two atoms, if it is possible. Order-independent
     *
     * @param atom1 First Atom
     * @param atom2 Second Atom
     * @return True if a reaction took place
     */
    public static boolean react(Atom atom1, Atom atom2, AbstractMap map, Simulator simulator) {

        //For testing purposes, this ReactionData is hardcoded in
        Set<ReactionData> reactions = map.enzymes.keySet();

        for (ReactionData reactionData : reactions) {
            if (reactionData.matchesPair(atom1, atom2)) {
                if (!simulator.doesBondCross(atom1, atom1.getLocation(), atom2, atom2.getLocation())) {
                    if (enzymeNearby(atom1, atom2, reactionData, map)) {

                        atom1.state = reactionData.postState1;
                        atom2.state = reactionData.postState2;

                        //The bond and unbond methods contain the checks for the pre-reaction states
                        if (reactionData.postBonded) {
                            atom1.bond(atom2);
                        } else {
                            atom2.unbond(atom1);
                        }

                        //Copies over reaction data
                        if (reactionData.copiesReaction) {

                            //Note that getReactions returns a shallow clone
                            map.updateEnzymes(atom2, atom1.getReactions());

                            atom2.setReactions(atom1.getReactions());

                            simulator.updateReactions(atom1.getLocation(), atom2.getLocation());
                        }
                        return true;
                    }
                }
            } else if (reactionData.matchesPair(atom2, atom1)) {
                //Recursion to switch positions of atoms
                return react(atom2, atom1, map, simulator);
            }
        }

        return false;
    }

    /**
     * Determines whether the reach of an enzyme is blocked by a membrane (defined as a bond between type-a atoms)
     *
     * @return true if the reaction is valid, false otherwise
     */
    public static boolean canEnzymeReach(AbstractMap map, Atom enzyme, Atom reactant1, Atom reactant2){
        HashSet<ILocation> crossedArea = map.getCrossedZone(enzyme.getLocation(), reactant1.getLocation());
        crossedArea.addAll(map.getCrossedZone(enzyme.getLocation(), reactant2.getLocation()));

        for(ILocation loc : crossedArea){
            Atom atom = map.getAtomAtLocation(loc);
            //Check for A-type atom
            if(atom != null && atom.type == EnumType.A){
                 for(Atom bondedAtom : atom.bonds){
                    if(bondedAtom.type == EnumType.A){
                        //Check for uniqueness
                        if(!(atom == reactant1 || atom == reactant2)){
                            if (map.crossed(reactant1.getLocation(), enzyme.getLocation(), atom.getLocation(), bondedAtom.getLocation(), true)){
                                return false;
                            }
                            if (map.crossed(reactant2.getLocation(), enzyme.getLocation(), atom.getLocation(), bondedAtom.getLocation(), true)){
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Given a reaction and a pair of two atoms
     * Determines whether an appropriate enzyme is nearby
     *
     * @param atom1    First atom in reaction
     * @param atom2    Second atom in reaction
     * @param reaction The reaction between the atoms. Note that this isn't determined exclusively by atom1 and atom2. Wildcard values are not taken into account
     * @param map      The map on which this reaction takes place
     * @return True if an appropriate enzyme is nearby
     */
    public static boolean enzymeNearby(Atom atom1, Atom atom2, ReactionData reaction, AbstractMap map) {
        //Cycle through all enzymes which contain a given reaction
        for (Atom enzyme : map.enzymes.get(reaction)) {
            //We want to check the distance to either product
            if (map.getDistance(enzyme.getLocation(), atom1.getLocation()) <= SimulatorConstants.ENZYME_RANGE ||
                    map.getDistance(enzyme.getLocation(), atom2.getLocation()) <= SimulatorConstants.ENZYME_RANGE) {
                 if(canEnzymeReach(map, enzyme, atom1, atom2)){
                    return true;
                }
            }
        }
        return false;
    }

}
