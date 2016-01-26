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
import adlytempleton.reaction.ReactionData;

import java.util.Random;

/**
 * Created by ATempleton on 1/9/2016.
 * <p>
 * A subclass of IMutation intended to make simpler basic mutations which act upon a single randomly determined reaction
 */
public abstract class SingleReactionMutation implements IMutation {
    @Override
    public ReactionData[] mutate(ReactionData[] original, Random random, Atom atom, AbstractMap map) {
        //Find reaction to mutate
        if (original.length > 0) {
            int i = random.nextInt(original.length);
            if (original[i] != null) {
                original[i] = mutateReaction(MutableReactionData.fromReaction(original[i]), random).toReaction();
            }
        }


        return original;
    }

    /**
     * Should mutate and return the singular ReactionData.
     * Contains the heavylifting of mutate for a SingleReactionMutation
     */
    protected abstract MutableReactionData mutateReaction(MutableReactionData reaction, Random random);

    @Override
    public int getWeight() {
        return 1;
    }

    @Override
    public boolean isValidMutation(ReactionData[] original) {
        return true;
    }
}
