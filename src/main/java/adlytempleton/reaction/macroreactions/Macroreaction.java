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
import java.util.List;

/**
 * Created by ATempleton on 1/29/2016.
 */
public abstract class Macroreaction {

    public Macroreaction(){

    }

    /**
     * @return an List of all reactions directly contained in this reaction
     */
    public abstract List<Macroreaction> getSubreactions();

    /*
     *  Returns a List of all reactions recursively contained in this reaction
     */
    public List<Macroreaction> flatten(){
        List<Macroreaction> result = new ArrayList<>();

        for(Macroreaction macroreaction : getSubreactions()){
            result.addAll(macroreaction.flatten());
        }

        return result;
    }

    public static Macroreaction fromString(String s) {
        char prefix = s.charAt(0);

        if(s.equals("null")){
            return null;
        }

        switch (prefix){
            case 'c':
                return ChainMacroreaction.fromString(s.substring(1));
            case 'g':
                return GliderMacroreaction.fromString(s.substring(1));
            case 's':
                return SupplementalMacroreaction.fromString(s.substring(1));
            case 'e':
                return new EmptyMacroreaction();
            default:
                return ReactionData.fromString(s);
        }
    }

    public static Macroreaction[] arrayFromString(String s){
        s = s.substring(1, s.length() - 1);

        String[] components = s.split(",");
        Macroreaction[] result = new Macroreaction[components.length];
        for(int i = 0; i < components.length; i++){
            result[i] = fromString(components[i]);
        }

        return result;

    }
}
