/*
 * Copyright 2016 Adly Templeton
 *
 * This file is part of the AChem Simulator.
 *
 * The AChem Simulator is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * The AChem Simulator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Foobar. If not, see http://www.gnu.org/licenses/.
 */

package adlytempleton.reaction;

import adlytempleton.atom.EnumType;

/**
 * Created by ATempleton on 1/9/2016.
 *
 * Helper class used to modify the contents of a ReactionData
 */
public class MutableReactionData {
    public EnumType type1;
    public EnumType type2;

    public int preState1;
    public int preState2;
    public int postState1;
    public int postState2;

    public boolean preBonded;
    public boolean postBonded;

    public boolean copiesReaction;

    public static MutableReactionData fromReaction(ReactionData rxn){
        if(rxn instanceof ReactionDataTriple) {
            ReactionDataTriple triple = (ReactionDataTriple) rxn;
            return new MutableReactionDataTriple(triple.type1, triple.type2, triple.type3, triple.preState1, triple.preState2, triple.preState3, triple.postState1, triple.postState2, triple.postState3
                    ,triple.preBonded, triple.preBonded23, triple.preBonded31, triple.postBonded, triple.postBonded23, triple.postBonded31);
        }else{
            return new MutableReactionData(rxn.type1, rxn.type2, rxn.preState1, rxn.preState2, rxn.postState1, rxn.postState2, rxn.preBonded, rxn.postBonded, rxn.copiesReaction);
        }    
       }

    public ReactionData toReaction(){
        return new ReactionData(type1, type2, preState1, preState2, postState1, postState2, preBonded, postBonded, copiesReaction);
    }

    /**
     * Constructs a reaction from all component elements
     *
     * @param type1      Type of atom 1
     * @param type2      Type of atom 2
     * @param preState1  State of atom 1 before reaction
     * @param preState2  State of atom 2 before reaction
     * @param postState1 State of atom 1 after reaction
     * @param postState2 State of atom 2 after reaction
     * @param preBonded  Are atoms bonded before reaction
     * @param postBonded Are atoms bonded after reaction
     */
    public MutableReactionData(EnumType type1, EnumType type2, int preState1, int preState2, int postState1, int postState2, boolean preBonded, boolean postBonded, boolean copiesReaction) {
        this.type1 = type1;
        this.type2 = type2;
        this.preState1 = preState1;
        this.preState2 = preState2;
        this.postState1 = postState1;
        this.postState2 = postState2;
        this.preBonded = preBonded;
        this.postBonded = postBonded;
        this.copiesReaction = copiesReaction;
    }
}
