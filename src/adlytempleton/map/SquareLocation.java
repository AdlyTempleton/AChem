package adlytempleton.map;

/**
 * Created by ATempleton on 11/7/2015.
 *
 * Basic implementation of an ILocation
 */
public class SquareLocation implements ILocation {

    @Override
    public int hashCode() {
        return 1023 * getX() + getY();
    }

    @Override
    public boolean equals(Object loc) {
        //Sanity Check
        if(!(loc instanceof SquareLocation)){
            return false;
        }

        SquareLocation sqLoc = (SquareLocation) loc;

        return sqLoc.getX() == getX() && sqLoc.getY() == getY();
    }

    private final int x;
    private final int y;

    public SquareLocation(int x, int y) {
        this.y = y;
        this.x = x;
    }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
}
