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

import adlytempleton.map.AbstractMap;
import adlytempleton.reaction.ReactionData;
import com.google.common.base.Predicates;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ATempleton on 1/15/2016.
 */
public class EnzymeMonitor {
    public static HashMap<Integer, Multiset<ReactionData>> records;
    /*
     * Enzymes present when file is loaded
     */
    protected static Set<ReactionData> baselineReactions;

    public static void loadBaselineReactions(AbstractMap map) {
        baselineReactions = new HashSet<>(map.enzymes.keySet());
    }

    /**
     * Returns the reactions that did not exist at world run
     * Uses the cached data
     */
    public static Multiset getNewReactions(AbstractMap map) {
        Multiset enzymes = Multisets.copyHighestCountFirst(map.enzymes.keys());
        if (baselineReactions != null) {
            enzymes = Multisets.filter(enzymes, Predicates.not(Predicates.in(baselineReactions)));
        }
        return enzymes;
    }
}
