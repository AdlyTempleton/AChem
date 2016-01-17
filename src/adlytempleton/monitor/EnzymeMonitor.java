package adlytempleton.monitor;

import adlytempleton.map.AbstractMap;
import adlytempleton.reaction.ReactionData;
import com.google.common.base.Predicates;
import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.collect.TreeMultiset;

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

    public static void loadBaselineReactions(AbstractMap map){
        baselineReactions = new HashSet<>(map.enzymes.keySet());
    }

    /**
     * Returns the reactions that did not exist at world load
     * Uses the cached data
     */
    public static Multiset getNewReactions(AbstractMap map){
        Multiset enzymes = Multisets.copyHighestCountFirst(map.enzymes.keys());
        if(baselineReactions != null) {
            enzymes = Multisets.filter(enzymes, Predicates.not(Predicates.in(baselineReactions)));
        }
        return enzymes;
    }
}
