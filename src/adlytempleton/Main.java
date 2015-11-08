package adlytempleton;

import adlytempleton.atom.Atom;
import adlytempleton.atom.EnumType;
import adlytempleton.map.AbstractMap;
import adlytempleton.map.Simulator;
import adlytempleton.map.SquareLocation;
import adlytempleton.map.SquareMap;

public class Main {

    public static void main(String[] args) {
        SquareMap map = new SquareMap(5);

        map.addAtom(new SquareLocation(1, 0), new Atom(EnumType.A));
        map.addAtom(new SquareLocation(1, 1), new Atom(EnumType.B));
        map.addAtom(new SquareLocation(1, 2), new Atom(EnumType.C));
        map.addAtom(new SquareLocation(1, 3), new Atom(EnumType.D));
        map.addAtom(new SquareLocation(1, 4), new Atom(EnumType.E));


        Simulator simulator = new Simulator(map);

        while(true){

            simulator.tick();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
