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
import adlytempleton.simulator.SimulatorConstants;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ATempleton on 1/9/2016.
 */
public class MutationManager {

    private static ArrayList<IMutation> mutations(){
        ArrayList<IMutation> mutations = new ArrayList<>();

        mutations.add(new BooleanFlagMutation());
        mutations.add(new DuplicationMutation());
        mutations.add(new PointStateMutation());
        mutations.add(new PointTypeMutation());

        return mutations;
    }

    public static void mutate(Atom atom, AbstractMap map){

        Random random = new Random();
        if(random.nextFloat() < SimulatorConstants.MUTATION_CHANCE){
            //Note that getReactions returns a shallow clone
            ReactionData[] reactions = atom.getReactions();

            int i = 0;
            IMutation selectedMutation = null;
            for(IMutation mutation : mutations()){
                if(mutation.isValidMutation(reactions)){
                    for(int j = 0; j < mutation.getWeight(); j++){
                        if(i == 0 || random.nextInt(i) == 0){
                            selectedMutation = mutation;
                        }
                        i++;
                    }
                }
            }

            atom.setReactions(selectedMutation.mutate(reactions, random, atom, map));
        }
    }
}
