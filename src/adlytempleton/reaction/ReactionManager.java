package adlytempleton.reaction;

import adlytempleton.atom.Atom;
import adlytempleton.atom.EnumType;

import java.util.ArrayList;

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
    public static boolean react(Atom atom1, Atom atom2){

        //For testing purposes, this ReactionData is hardcoded in
        ArrayList<ReactionData> reactions = new ArrayList<>();
        reactions.add(new ReactionData(EnumType.X, EnumType.X, 0, 0, 1, 2, false, true));


        for(ReactionData reactionData : reactions)
        if(reactionData.matchesPair(atom1, atom2)){

            atom1.state = reactionData.postState1;
            atom2.state = reactionData.postState2;

            //The bond and unbond methods contain the checks for the pre-reaction states
            if(reactionData.postBonded){
                atom1.bond(atom2);
            }else{
                atom2.unbond(atom1);
            }
            return true;
        }else if(reactionData.matchesPair(atom2, atom1)){
            //Recursion to switch positions of atoms
            return react(atom2, atom1);
        }

        return false;
    }

}
