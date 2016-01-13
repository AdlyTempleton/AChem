package adlytempleton.mutation;

import adlytempleton.reaction.MutableReactionData;
import adlytempleton.reaction.MutableReactionDataTriple;
import adlytempleton.reaction.ReactionDataTriple;

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

        if(reaction instanceof MutableReactionDataTriple){
            reaction.copiesReaction = false;

            MutableReactionDataTriple rxn = (MutableReactionDataTriple) reaction;
            rxn.postBonded23 = random.nextBoolean();
            rxn.postBonded31 = random.nextBoolean();
            rxn.preBonded23 = random.nextBoolean();
            rxn.preBonded23 = random.nextBoolean();

            return rxn;
        }



        return reaction;
    }
}
