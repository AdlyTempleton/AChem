package adlytempleton.mutation;

import adlytempleton.atom.EnumType;
import adlytempleton.reaction.MutableReactionData;

import java.util.Random;

/**
 * Created by ATempleton on 1/9/2016.
 */
public class PointTypeMutation extends SingleReactionMutation {
    @Override
    protected MutableReactionData mutateReaction(MutableReactionData reaction, Random random) {
        EnumType type = EnumType.standardTypes().get(random.nextInt(EnumType.standardTypes().size()));

        //Whether to modify reaction 1 or reaction 2
        if(random.nextBoolean()){
            reaction.type1 = type;
        }else{
            reaction.type2 = type;
        }

        return reaction;
    }
}
