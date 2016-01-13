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
