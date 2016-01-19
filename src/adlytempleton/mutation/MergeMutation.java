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

package adlytempleton.mutation;

import adlytempleton.atom.Atom;
import adlytempleton.map.AbstractMap;
import adlytempleton.reaction.MutableReactionData;
import adlytempleton.reaction.MutableReactionDataTriple;
import adlytempleton.reaction.ReactionData;
import adlytempleton.reaction.ReactionDataTriple;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ATempleton on 1/17/2016.
 *
 * Merges multiple refrences of states into one
 */
public class MergeMutation implements IMutation{
    @Override
    public ReactionData[] mutate(ReactionData[] original, Random random, Atom atom, AbstractMap map) {
        ArrayList<Integer> statesPresent = new ArrayList<>();
        for(ReactionData rxn : original) {
            if(rxn != null){
                statesPresent.add(rxn.preState1);
                statesPresent.add(rxn.preState2);
                statesPresent.add(rxn.postState1);
                statesPresent.add(rxn.postState2);

                if(rxn instanceof ReactionDataTriple) {
                    statesPresent.add(((ReactionDataTriple) rxn).preState3);
                    statesPresent.add(((ReactionDataTriple) rxn).postState3);
                }
            }
        }

        if(statesPresent.size() > 0) {
            int from = statesPresent.get(random.nextInt(statesPresent.size()));
            int to = statesPresent.get(random.nextInt(statesPresent.size()));

            for(int i = 0; i < original.length; i++){
                ReactionData rxn = original[i];
                if(rxn != null){
                    MutableReactionData mutableRxn = MutableReactionData.fromReaction(rxn);

                    mutableRxn.preState1 = mutableRxn.preState1 == from ? to : mutableRxn.preState1;
                    mutableRxn.postState1 = mutableRxn.postState1 == from ? to : mutableRxn.postState1;
                    mutableRxn.preState2 = mutableRxn.preState2 == from ? to : mutableRxn.preState2;
                    mutableRxn.postState2 = mutableRxn.postState2 == from ? to : mutableRxn.postState2;


                    if(mutableRxn instanceof MutableReactionDataTriple) {

                        MutableReactionDataTriple mutableRxn3 = (MutableReactionDataTriple) mutableRxn;

                        mutableRxn3.preState3 = mutableRxn3.preState3 == from ? to : mutableRxn3.preState3;
                        mutableRxn3.postState3 = mutableRxn3.postState3 == from ? to : mutableRxn3.postState3;
                    }


                    original[i] = mutableRxn.toReaction();
                }

            }


        }

        return original;
    }

    @Override
    public int getWeight() {
        return 2;
    }

    @Override
    public boolean isValidMutation(ReactionData[] original) {
        return true;
    }
}
