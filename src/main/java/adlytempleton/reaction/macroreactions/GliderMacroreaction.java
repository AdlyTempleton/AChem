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

import java.util.Arrays;
import java.util.List;

/**
 * Created by ATempleton on 1/29/2016.
 */
public class GliderMacroreaction extends Macroreaction {
    public Macroreaction[] init;
    public Macroreaction[] propagate;
    public Macroreaction[] actions;
    public Macroreaction[] bounce;

    @Override
    public List<Macroreaction> getSubreactions() {
        List<Macroreaction> result = Arrays.<Macroreaction>asList(init);
        result.addAll(Arrays.asList(propagate));
        result.addAll(Arrays.asList(actions));
        result.addAll(Arrays.asList(bounce));
        return result;
    }

    public static GliderMacroreaction fromString(String s) {
        s = s.substring(1, s.length() - 1);

        String[] components = s.split(",");
        Macroreaction[][] result = new Macroreaction[components.length][];
        for(int i = 0; i < components.length; i++){
            result[i] = arrayFromString(components[i]);
        }

        GliderMacroreaction gliderMacroreaction = new GliderMacroreaction();
        gliderMacroreaction.init = result[0];
        gliderMacroreaction.propagate = result[1];
        gliderMacroreaction.actions = result[2];
        gliderMacroreaction.bounce = result[3];

        return gliderMacroreaction;
    }
}
