package adlytempleton.monitor;

import adlytempleton.atom.Atom;
import adlytempleton.map.AbstractMap;

/**
 * Created by ATempleton on 3/9/2016.
 */
public class Biomass {

    public static float calculateBiomassPercentage(AbstractMap map){
        int totalAtoms = map.getAllAtoms().size();

        int nonFoodAtoms = 0;
        for(Atom atom : map.getAllAtoms()){
            if(atom.state != 0 || atom.isEnzyme() || atom.bonds.size() > 0){
                nonFoodAtoms += 1;
            }
        }

        return ((float) nonFoodAtoms) / ((float) totalAtoms);

    }
}
