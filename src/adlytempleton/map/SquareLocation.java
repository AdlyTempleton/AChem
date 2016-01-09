package adlytempleton.map;

/**
 * Created by ATempleton on 11/7/2015.
 * <p>
 * Basic implementation of an ILocation for a square grid
 */
public class SquareLocation implements ILocation {

    private final int x;
    private final int y;

    public SquareLocation(int x, int y) {
        this.y = y;
        this.x = x;
    }

    public SquareLocation() {
        this(0, 0);
    }

    @Override
    public ILocation add(ILocation other) {
        assert other instanceof SquareLocation;

        return new SquareLocation(((SquareLocation) other).x + x, ((SquareLocation) other).y + y);
    }

    @Override
    public int distance(ILocation other) {
        assert other instanceof SquareLocation;
        SquareLocation otherLoc = (SquareLocation) other;

        return Math.max(Math.abs(otherLoc.getX() - getX()), Math.abs(otherLoc.getY() - getY()));
    }

    @Override
    public ILocation subtract(ILocation other) {
        assert other instanceof SquareLocation;
        return new SquareLocation(((SquareLocation) other).x - x, ((SquareLocation) other).y - y);
    }

    @Override
    public int hashCode() {
        return 1023 * getX() + getY();
    }

    @Override
    public boolean equals(Object loc) {
        //Sanity Check
        if (!(loc instanceof SquareLocation)) {
            return false;
        }

        SquareLocation sqLoc = (SquareLocation) loc;

        return sqLoc.getX() == getX() && sqLoc.getY() == getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
