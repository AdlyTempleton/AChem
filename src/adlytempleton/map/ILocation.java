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
     * ie. other location - this location
     **/
    ILocation subtract(ILocation other);
}
