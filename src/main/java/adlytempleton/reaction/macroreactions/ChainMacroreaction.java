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

package adlytempleton.reaction.macroreactions;

import adlytempleton.reaction.ReactionData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ATempleton on 1/29/2016.
 */
public class ChainMacroreaction extends Macroreaction {

    //The first dimension is steps in the chain reaction
    //The second dimension is the parallel reactions within that step
    public Macroreaction[][] chain;

    @Override
    public List<Macroreaction> getSubreactions() {
        List<Macroreaction> result = new ArrayList<>();
        for(Macroreaction[] step : chain){
            result.addAll(Arrays.asList(step));
        }
        return result;
    }

    public static ChainMacroreaction fromString(String s){
        s = s.substring(1, s.length() - 1);

        String[] components = s.split(",");
        Macroreaction[][] result = new Macroreaction[components.length][];
        for(int i = 0; i < components.length; i++){
            result[i] = arrayFromString(components[i]);
        }

        ChainMacroreaction chainMacroreaction = new ChainMacroreaction();
        chainMacroreaction.chain = result;
        return chainMacroreaction;
    }
}
