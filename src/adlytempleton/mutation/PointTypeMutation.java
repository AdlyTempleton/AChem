package adlytempleton.mutation;

import adlytempleton.atom.EnumType;
import adlytempleton.reaction.MutableReactionData;
import adlytempleton.reaction.MutableReactionDataTriple;

import java.util.Random;

/**
 * Created by ATempleton on 1/9/2016.
 */
public class PointTypeMutation extends SingleReactionMutation {
    @Override
    protected MutableReactionData mutateReaction(MutableReactionData reaction, Random random) {
        EnumType type = EnumType.values()[random.nextInt(EnumType.values().length)];

        //Whether to modify reaction 1 or reaction 2
        if(reaction instanceof MutableReactionDataTriple){
            switch (random.nextInt(3)){
                case 0:
                    reaction.type1 = type;
                    break;
                case 1:
                    reaction.type2 = type;
                    break;
                case 2:
                    ((MutableReactionDataTriple) reaction).type3 = type;
                    break;
            }
        }else {
            if (random.nextBoolean()) {
                reaction.type1 = type;
            } else {
                reaction.type2 = type;
            }
        }

        return reaction;
    }
}
