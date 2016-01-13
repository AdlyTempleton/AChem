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
        for(int i = 0; i < original.length; i++){
            if(original[i] == null){
                original[i] = (ReactionData) original[random.nextInt(original.length)];

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
        for(ReactionData data : original){
            if(data == null){
                return true;
            }
        }
        return false;
    }
}
