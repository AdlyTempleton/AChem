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

import adlytempleton.reaction.MutableReactionData;
import adlytempleton.reaction.MutableReactionDataTriple;
import adlytempleton.reaction.ReactionData;

import java.util.Random;

/**
 * Created by ATempleton on 1/9/2016.
 */
public class PointStateMutation extends SingleReactionMutation {

    @Override
    protected MutableReactionData mutateReaction(MutableReactionData rxn, Random random) {
        //The reaction to mutate: prestate1 - prestate2 - poststate1 - poststate2
        int stateToMutate = rxn instanceof MutableReactionDataTriple ? random.nextInt(6) : random.nextInt(4);

        //The offset should be a integer value randomly and normally distributed around 0
        //With a standard deviation of 10
        //Note that the offset must be nonzero
        int offset = 0;
        while (offset == 0) {
            offset = (int) Math.round(random.nextGaussian() * 10);
        }

        switch (stateToMutate) {
            case 0:
                rxn.preState1 += offset;
                rxn.preState1 = Math.abs(rxn.preState1);
                break;

            case 1:
                rxn.preState2 += offset;
                rxn.preState2 = Math.abs(rxn.preState2);
                break;

            case 2:
                rxn.postState1 += offset;
                rxn.postState1 = Math.abs(rxn.postState1);
                break;

            case 3:
                rxn.postState2 += offset;
                rxn.postState2 = Math.abs(rxn.postState2);
                break;
            case 4:
                ((MutableReactionDataTriple)rxn).preState3 += offset;
                ((MutableReactionDataTriple)rxn).preState3 = Math.abs(((MutableReactionDataTriple)rxn).preState3);
                break;
            case 5:
                ((MutableReactionDataTriple)rxn).postState3 += offset;
                ((MutableReactionDataTriple)rxn).postState3 = Math.abs(((MutableReactionDataTriple)rxn).preState3);
                break;
        }

        return rxn;
    }
}
