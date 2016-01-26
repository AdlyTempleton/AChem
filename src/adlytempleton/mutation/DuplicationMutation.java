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
 * Created by ATempleton on 1/12/2016.
 */
public class DuplicationMutation implements IMutation {
    @Override
    public ReactionData[] mutate(ReactionData[] original, Random random, Atom atom, AbstractMap map) {
        for (int i = 0; i < original.length; i++) {
            if (original[i] == null) {
                original[i] = original[random.nextInt(original.length)];

            }
        }

        return original;
    }

    @Override
    public int getWeight() {
        return 1;
    }

    @Override
    public boolean isValidMutation(ReactionData[] original) {
        for (ReactionData data : original) {
            if (data == null) {
                return true;
            }
        }
        return false;
    }
}
