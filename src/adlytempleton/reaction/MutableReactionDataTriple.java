package adlytempleton.reaction;

import adlytempleton.atom.EnumType;

/**
 * Created by ATempleton on 1/12/2016.
 */
public class MutableReactionDataTriple extends MutableReactionData {
    public EnumType type3;
    public int preState3;
    public int postState3;

    public boolean preBonded23;
    public boolean preBonded31;

    public boolean postBonded23;
    public boolean postBonded31;

    public MutableReactionDataTriple(EnumType type1, EnumType type2, EnumType type3, int preState1, int preState2, int prestate3, int postState1, int postState2, int postState3, boolean preBonded, boolean preBonded23, boolean preBonded31, boolean postBonded, boolean postBonded23, boolean postBonded31) {
        super(type1, type2, preState1, preState2, postState1, postState2, preBonded, postBonded, false);

        this.type3 = type3;
        this.preState3 = prestate3;
        this.postState3 = postState3;
        this.preBonded23 = preBonded23;
        this.preBonded31 = preBonded31;
        this.postBonded23 = postBonded23;
        this.postBonded31 = postBonded31;
    }
}
