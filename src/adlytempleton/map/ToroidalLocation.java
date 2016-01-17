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
 * Created by ATempleton on 1/12/2016.
 */
public class ToroidalLocation extends SquareLocation {

    public int mapSize;

    public ToroidalLocation(SquareLocation squareLocation, int mapSize) {
        this(squareLocation.getX(), squareLocation.getY(), mapSize);
    }

    public ToroidalLocation(int x, int y, int mapSize) {
        super((x + mapSize) % mapSize, (y + mapSize) % mapSize);
        this.mapSize = mapSize;
    }

    @Override
    public ILocation subtract(ILocation other) {
        assert other instanceof SquareLocation;
        SquareLocation loc = (SquareLocation) other;
        return new ToroidalLocation(loc.getX() - getX(), loc.getY() - getY(), mapSize);
    }

    @Override
    public ILocation add(ILocation other) {
        assert other instanceof SquareLocation;
        SquareLocation loc = (SquareLocation) other;
        return new ToroidalLocation(loc.getX() + getX(), loc.getY() + getY(), mapSize);
    }

    @Override
    public int distance(ILocation other) {
        assert other instanceof ToroidalLocation;

        SquareLocation transformedLoc = ((ToroidalLocation)other).compareTo(this);

        return super.distance(transformedLoc);
    }

    /**
     * Returns the equivalent representation of these toroidal coordinates which is closest to the given ToroidalLocation
     */
    public SquareLocation compareTo(ToroidalLocation other){
        int transformedX = getX();
        int transformedY = getY();

        int border = mapSize / 2;
        if(other.getX() < border && getX() > border){
            transformedX = getX() - mapSize;
        }else if(other.getX() > border & getX() < border){
            transformedX = getX() + mapSize;
        }

        if(other.getY() < border && getY() > border){
            transformedY = getY() - mapSize;
        }else if(other.getY() > border && getX() < border){
            transformedY = getY() + mapSize;
        }

        return new SquareLocation(transformedX, transformedY);
    }

    public ToroidalLocation(int x, int y) {
        super(x, y);
    }
}
