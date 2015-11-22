package adlytempleton.reaction;

import adlytempleton.atom.Atom;
import adlytempleton.atom.EnumType;
import adlytempleton.map.AbstractMap;
import adlytempleton.simulator.SimulatorConstants;

import java.util.ArrayList;
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
     *
     * @return True if a reaction took place
     */
    public static boolean react(Atom atom1, Atom atom2, AbstractMap map){

        //For testing purposes, this ReactionData is hardcoded in
        Set<ReactionData> reactions = map.enzymes.keySet();

        System.out.println(new ReactionData(EnumType.X, EnumType.Y, 1, 1, 3, 3, false, true));


        for(ReactionData reactionData : reactions) {
            if (reactionData.matchesPair(atom1, atom2)) {
                if(enzymeNearby(atom1, atom2, reactionData, map)) {

                    atom1.state = reactionData.postState1;
                    atom2.state = reactionData.postState2;

                    //The bond and unbond methods contain the checks for the pre-reaction states
                    if (reactionData.postBonded) {
                        atom1.bond(atom2);
                    } else {
                        atom2.unbond(atom1);
                    }
                    return true;
                }
            } else if (reactionData.matchesPair(atom2, atom1)) {
                //Recursion to switch positions of atoms
                return react(atom2, atom1, map);
            }
        }

        return false;
    }

    /**
     * Given a reaction and a pair of two atoms
     * Determines whether an appropriate enzyme is nearby
     * @param atom1 First atom in reaction
     * @param atom2 Second atom in reaction
     * @param reaction The reaction between the atoms. Note that this isn't determined exclusively by atom1 and atom2. Wildcard values are not taken into account
     * @param map The map on which this reaction takes place
     * @return True if an appropriate enzyme is nearby
     */
    public static boolean enzymeNearby(Atom atom1, Atom atom2, ReactionData reaction, AbstractMap map){
        for(Atom enzyme : map.enzymes.get(reaction)){
            if(map.getDistance(enzyme.getLocation(), atom1.getLocation()) <= SimulatorConstants.ENZYME_RANGE ||
                    map.getDistance(enzyme.getLocation(), atom2.getLocation()) <= SimulatorConstants.ENZYME_RANGE){
                return true;
            }
        }
        return false;
    }

}
