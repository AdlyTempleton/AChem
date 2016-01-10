package adlytempleton.mutation;

import adlytempleton.reaction.MutableReactionData;
import adlytempleton.reaction.ReactionData;

import java.util.Random;

/**
 * Created by ATempleton on 1/9/2016.
 */
public class PointStateMutation extends SingleReactionMutation {

    @Override
    protected MutableReactionData mutateReaction(MutableReactionData rxn, Random random) {
        //The reaction to mutate: prestate1 - prestate2 - poststate1 - poststate2
        int stateToMutate = random.nextInt(4);

        //The offset should be a integer value randomly and normally distributed around 0
        //Note that the offset must be nonzero
        int offset = 0;
        while (offset == 0) {
            offset = (int) Math.round(random.nextGaussian() * 5);
        }

        switch (stateToMutate) {
            case 0:
                rxn.preState1 += offset;
                rxn.preState1 = Math.max(0, rxn.preState1);
                break;

            case 1:
                rxn.preState2 += offset;
                rxn.preState2 = Math.max(0, rxn.preState2);
                break;

            case 2:
                rxn.postState1 += offset;
                rxn.postState1 = Math.max(0, rxn.postState1);
                break;

            case 3:
                rxn.postState2 += offset;
                rxn.postState2 = Math.max(0, rxn.postState2);
                break;
        }

        return rxn;
    }
}
