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
