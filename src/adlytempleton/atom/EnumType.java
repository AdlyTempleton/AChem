package adlytempleton.atom;

/**
 * Created by ATempleton on 11/7/2015.
 *
 * Represents the types of atom a-f
 */
public enum EnumType {
    A('A'), B('B'), C('C'), D('D'), E('E'), F('F');


    public char symbol;

    EnumType(char symbol){
        this.symbol = symbol;
    }
}
