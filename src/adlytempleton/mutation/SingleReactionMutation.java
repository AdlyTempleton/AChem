package adlytempleton.mutation;

import adlytempleton.atom.Atom;
import adlytempleton.map.AbstractMap;
import adlytempleton.reaction.MutableReactionData;
import adlytempleton.reaction.ReactionData;

import java.util.Random;

/**
 * Created by ATempleton on 1/9/2016.
 *
 * A subclass of IMutation intended to make simpler basic mutations which act upon a single randomly determined reaction
 */
public abstract class SingleReactionMutation implements IMutation{
    @Override
    public ReactionData[] mutate(ReactionData[] original, Random random, Atom atom, AbstractMap map) {
        //Find reaction to mutate
        int i = random.nextInt(original.length);
        if(original[i] != null) {
            original[i] = mutateReaction(MutableReactionData.fromReaction(original[i]), random).toReaction();
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
