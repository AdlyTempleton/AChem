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

package adlytempleton.map;

import adlytempleton.atom.Atom;
import com.sun.corba.se.impl.orb.ParserTable;

import java.util.*;

/**
 * Created by ATempleton on 1/12/2016.
 */
public class ToroidalMap extends SquareMap {
    public ToroidalMap(int size, boolean showGUI) {
        super(size);
    }

    @Override
    public Atom getAtomAtLocation(ILocation location) {
        SquareLocation sqloc = (SquareLocation) location;
        SquareLocation transferedLoc = new SquareLocation(sqloc.getX() % getSize(), sqloc.getY() % getSize());
        return super.getAtomAtLocation(transferedLoc);
    }

    @Override
    public HashSet<ILocation> getCrossedZone(ILocation loc1, ILocation loc2) {
        assert loc1 instanceof ToroidalLocation;
        assert loc2 instanceof ToroidalLocation;

        return super.getCrossedZone(loc1, ((ToroidalLocation) loc2).compareTo((ToroidalLocation) loc1));
    }

    @Override
    public boolean crossed(ILocation loc11, ILocation loc12, ILocation loc21, ILocation loc22, boolean strict) {
        assert loc11 instanceof ToroidalLocation;
        assert loc12 instanceof ToroidalLocation;
        assert loc21 instanceof ToroidalLocation;
        assert loc22 instanceof ToroidalLocation;

        SquareLocation transformedLoc12 = ((ToroidalLocation) loc12).compareTo((ToroidalLocation) loc11);
        SquareLocation transformedLoc21 = ((ToroidalLocation) loc21).compareTo((ToroidalLocation) loc11);
        SquareLocation transformedLoc22 = ((ToroidalLocation) loc22).compareTo((ToroidalLocation) loc11);

        return super.crossed(loc11, transformedLoc12, transformedLoc21, transformedLoc22, strict);

    }

    @Override
    public Class<? extends ILocation> getLocationType() {
        return ToroidalLocation.class;
    }

    public int getDistance(ILocation loc1, ILocation loc2) {

        return loc1.distance(loc2);
    }

    @Override
    public ArrayList<ILocation> getLocationsWithinRange(ILocation location, int range) {
        ArrayList<ILocation> locations = super.getLocationsWithinRange(location, range);

        ArrayList<ILocation> result = new ArrayList<>();

        for(ILocation iLocation : locations){
            result.add(new ToroidalLocation((SquareLocation) iLocation, getSize()));
        }

        return result;
    }

    @Override
    public ArrayList<ILocation> getAllLocations() {
        ArrayList result = new ArrayList();

        for (int x = 0; x < getSize(); x++) {
            for (int y = 0; y < getSize(); y++) {
                result.add(new ToroidalLocation(x, y, getSize()));
            }
        }
        return result;
    }

    @Override
    public boolean isOnGrid(ILocation location) {
        return true;
    }

    @Override
    public List<ILocation> newlyInRange(ILocation startLoc, ILocation endLoc, int range) {
        assert endLoc instanceof ToroidalLocation;
        assert startLoc instanceof ToroidalLocation;

        ToroidalLocation end = (ToroidalLocation) endLoc;
        SquareLocation start = ((ToroidalLocation) startLoc).compareTo(end);

        int deltaX = end.getX() - start.getX();
        int deltaY = end.getY() - start.getY();

        LinkedList<ILocation> result = new LinkedList<>();

        //We want a line of length 2*range + 1
        //This line is the edge of all cells contained within the range
        //If we shift in multiple dimensions, we simply take the union
        if (deltaX != 0) {
            //deltaX is used as the sign
            int x = end.getX() + range * deltaX;
            for (int y = end.getY() - range; y <= end.getY() + range; y++) {
                result.add(new ToroidalLocation(x, y));
            }
        }

        //Same calculations as above
        if (deltaY != 0) {
            int y = end.getY() + range * deltaY;
            for (int x = end.getX() - range; x <= end.getX() + range; x++) {
                result.add(new ToroidalLocation(x, y));
            }
        }

        //Remove the overlap
        if (deltaX != 0 && deltaY != 0) {
            result.remove(new ToroidalLocation(end.getX() + deltaX * range, end.getY() + deltaY * range));
        }

        //Check to make sure that all elements are contained in the list
        Iterator iter = result.iterator();
        while (iter.hasNext()) {
            if (!isOnGrid((ILocation) iter.next())) {
                iter.remove();
            }
        }

        return result;
    }
}
