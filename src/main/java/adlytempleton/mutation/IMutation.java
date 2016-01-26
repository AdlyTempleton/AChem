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
import adlytempleton.reaction.ReactionData;

import java.util.Random;

/**
 * Created by ATempleton on 1/9/2016.
 */
public interface IMutation {
    /**
     * Runs the mutation on an array of given reactions
     * The given ReactionData may be modified and returned
     * This method may not always return a mutated reaction set
     * Mutation chance is handled at a higher level
     * The ReactionData[] should be a shallow clone
     *
     * @return The modified enzyme
     */
    ReactionData[] mutate(ReactionData[] original, Random random, Atom atom, AbstractMap map);


    /**
     * Returns the weight of this mutation (in selecting a mutation to be used
     */
    int getWeight();

    /**
     * Returns true if this is a valid mutation for the given ReactionData[]
     */
    boolean isValidMutation(ReactionData[] original);
}
