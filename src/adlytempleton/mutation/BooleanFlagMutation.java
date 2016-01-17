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
import adlytempleton.reaction.ReactionDataTriple;

import java.util.Random;

/**
 * Created by ATempleton on 1/9/2016.
 */
public class BooleanFlagMutation extends SingleReactionMutation {
    @Override
    protected MutableReactionData mutateReaction(MutableReactionData reaction, Random random) {
        reaction.preBonded = random.nextBoolean();
        reaction.postBonded = random.nextBoolean();
        reaction.copiesReaction = random.nextInt(10) == 0;

        if(reaction instanceof MutableReactionDataTriple){
            reaction.copiesReaction = false;

            MutableReactionDataTriple rxn = (MutableReactionDataTriple) reaction;
            rxn.postBonded23 = random.nextBoolean();
            rxn.postBonded31 = random.nextBoolean();
            rxn.preBonded23 = random.nextBoolean();
            rxn.preBonded23 = random.nextBoolean();

            return rxn;
        }



        return reaction;
    }
}
