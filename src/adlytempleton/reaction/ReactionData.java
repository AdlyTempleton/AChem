package adlytempleton.reaction;

import adlytempleton.atom.Atom;
import adlytempleton.atom.EnumType;

/**
 * Created by ATempleton on 11/14/2015.
 */
public class ReactionData {

    EnumType type1;
    EnumType type2;

    int preState1;
    int preState2;
    int postState1;
    int postState2;

    boolean preBonded;
    boolean postBonded;

    /**
     * Constructs a reaction from all component elements
     *
     * @param type1 Type of atom 1
     * @param type2 Type of atom 2
     * @param preState1 State of atom 1 before reaction
     * @param preState2 State of atom 2 before reaction
     * @param postState1 State of atom 1 after reaction
     * @param postState2 State of atom 2 after reaction
     * @param preBonded Are atoms bonded before reaction
     * @param postBonded Are atoms bonded after reaction
     */
    public ReactionData(EnumType type1, EnumType type2, int preState1, int preState2, int postState1, int postState2, boolean preBonded, boolean postBonded) {
        this.type1 = type1;
        this.type2 = type2;
        this.preState1 = preState1;
        this.preState2 = preState2;
        this.postState1 = postState1;
        this.postState2 = postState2;
        this.preBonded = preBonded;
        this.postBonded = postBonded;
    }

    /**
     * Checks if a ReactionData is applicable to a given pair of atoms. This is order-independent
     * @param a1 One Atom
     * @param a2 Another Atom
     * @return True if this reaction describes a reaction between those atoms
     */
    public boolean matches(Atom a1, Atom a2){
        return matchesPair(a1, a2) || matchesPair(a2, a1);
    }

    /**
     * Checks if a ReactionData is applicable to a given pair of atoms. This is order-specific
     * @param a1 First Atom
     * @param a2 Second Atom
     * @return True if atom is valid
     */
    public boolean matchesPair(Atom a1, Atom a2){

        //If the reaction applies to any two atoms of the same type
        boolean sameTypes = type1.isFlexible() && type1 == type2;

        //Types of atoms are valid
        boolean typesMatch = (sameTypes && a1.type == a2.type) || (!sameTypes && a1.type.matches(type1) && a2.type.matches(type2));

        //States of atoms are valid
        boolean statesMatch = a1.state == preState1 && a2.state == preState1;

        boolean bondsMatch = a1.bonds.contains(a2) == preBonded;

        return typesMatch && statesMatch && bondsMatch;

    }

}
