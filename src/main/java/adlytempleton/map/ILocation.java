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

/**
 * Created by ATempleton on 11/7/2015.
 * <p>
 * An abstract non-mutable class that represents a single location on a map (Such as a cartesian coordinate on a square grid
 * <p>
 * For flexibility, details of implementation are left almost entierly to the new location
 * <p>
 * Must implement equals() and hashCode()
 */
public interface ILocation {

    /**
     * @param other Another ILocation of the same type
     * @return The distance between this location and other(via the appropriate metric for the grid type)
     */
    int distance(ILocation other);

    /*
        Returns this location offset by other (treated as a vector)
     */
    ILocation add(ILocation other);

    /**
     * Returns the offset that must be added to other to get this location
     * ie. this location - other location
     **/
    ILocation subtract(ILocation other);
}
