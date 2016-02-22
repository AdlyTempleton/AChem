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

package adlytempleton.monitor;

import adlytempleton.atom.Atom;
import adlytempleton.map.AbstractMap;
import adlytempleton.map.Simulator;
import adlytempleton.reaction.ReactionData;
import adlytempleton.simulator.SimulatorConstants;
import com.google.common.base.Predicates;
import com.google.common.collect.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ATempleton on 1/15/2016.
 */
public class EnzymeMonitor {
    /*
     * Enzymes present when file is loaded
     */
    protected static Set<ReactionData> baselineReactions;

    public static void loadBaselineReactions(AbstractMap map) {
        baselineReactions = new HashSet<ReactionData>();
        //Add reactions from all states
        for(int i = 0; i < SimulatorConstants.MAX_STATE; i++){
            baselineReactions.addAll(map.enzymes.get(i).keySet());
        }
    }

    /**
     * Returns the reactions that did not exist at world run
     * Uses the cached data
     */
    public static Multiset getNewReactions(AbstractMap map) {

        Multiset enzymes = HashMultiset.create();

        for(Multimap<ReactionData, Atom> multimap : map.enzymes){
            for(ReactionData rxn : multimap.keys()){
                if(!enzymes.contains(rxn)){
                    enzymes.add(rxn, multimap.keys().count(rxn));
                }
            }
        }

        if (baselineReactions != null) {
            enzymes = Multisets.filter(enzymes, Predicates.not(Predicates.in(baselineReactions)));
        }
        return enzymes;
    }
}
