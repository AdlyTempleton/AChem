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
     * @return The modified enzyme
     */
    public ReactionData[] mutate(ReactionData[] original, Random random, Atom atom, AbstractMap map);


    /**
     * Returns the weight of this mutation (in selecting a mutation to be used
     */
    public int getWeight();

    /**
     * Returns true if this is a valid mutation for the given ReactionData[]
     */
    public boolean isValidMutation(ReactionData[] original);
}
