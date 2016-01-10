package adlytempleton.mutation;

import adlytempleton.reaction.MutableReactionData;

import java.util.Random;

/**
 * Created by ATempleton on 1/9/2016.
 */
public class BooleanFlagMutation extends SingleReactionMutation {
    @Override
    protected MutableReactionData mutateReaction(MutableReactionData reaction, Random random) {
        reaction.preBonded = random.nextBoolean();
        reaction.postBonded = random.nextBoolean();
        reaction.copiesReaction = random.nextInt(10) == 0;
        return reaction;
    }
}
