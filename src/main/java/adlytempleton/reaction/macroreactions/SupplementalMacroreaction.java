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
public class SupplementalMacroreaction extends Macroreaction{

    Macroreaction[] reactions;

    @Override
    public List<Macroreaction> getSubreactions() {
        return Arrays.<Macroreaction>asList(reactions);
    }

    public static SupplementalMacroreaction fromString(String s){
        SupplementalMacroreaction result = new SupplementalMacroreaction();
        result.reactions = Macroreaction.arrayFromString(s);
        return result;
    }
}
