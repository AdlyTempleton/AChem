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

import adlytempleton.map.Simulator;
import adlytempleton.reaction.ReactionData;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Created by ATempleton on 1/20/2016.
 */
public class EventTracker {

    public static ReactionData[] monitoredReactions = new ReactionData[]{ReactionData.fromString(
            "X5 + X0 to 6 - 6 (cpy)")};

    //The ticks at which each ReactionData was activated
    public static Multimap<ReactionData, Integer> records = HashMultimap.create(monitoredReactions.length, 1000);


    public static void notifyOfReaction(ReactionData activatedReaction){
        for(ReactionData data : monitoredReactions){
            if(activatedReaction.equals(data)){
                records.put(data, Simulator.ticks);
                return;
            }
        }
    }

    /**
     * Returns the number of recorded events of data between the times of start (inclusive) and end (exclusive)
     */
    public static int eventsWithinPeriod(ReactionData data, int start, int end){

        int count = 0;

        for(int i : records.get(data)){
            if(i >= start && i < end){
                count ++;
            }
        }

        return count;
    }

}
