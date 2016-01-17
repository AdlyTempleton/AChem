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

import adlytempleton.atom.EnumType;
import adlytempleton.reaction.MutableReactionData;
import adlytempleton.reaction.MutableReactionDataTriple;

import java.util.Random;

/**
 * Created by ATempleton on 1/9/2016.
 */
public class PointTypeMutation extends SingleReactionMutation {
    @Override
    protected MutableReactionData mutateReaction(MutableReactionData reaction, Random random) {
        EnumType type = EnumType.valuesExcludingCaustic()[random.nextInt(EnumType.valuesExcludingCaustic().length)];

        //Whether to modify reaction 1 or reaction 2
        if(reaction instanceof MutableReactionDataTriple){
            switch (random.nextInt(3)){
                case 0:
                    reaction.type1 = type;
                    break;
                case 1:
                    reaction.type2 = type;
                    break;
                case 2:
                    ((MutableReactionDataTriple) reaction).type3 = type;
                    break;
            }
        }else {
            if (random.nextBoolean()) {
                reaction.type1 = type;
            } else {
                reaction.type2 = type;
            }
        }

        return reaction;
    }
}
