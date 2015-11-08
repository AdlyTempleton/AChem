package adlytempleton;

import adlytempleton.atom.Atom;
import adlytempleton.atom.EnumType;
import adlytempleton.map.AbstractMap;
import adlytempleton.map.Simulator;
import adlytempleton.map.SquareLocation;
import adlytempleton.map.SquareMap;

public class Main {

    public static void main(String[] args) {
        SquareMap map = new SquareMap(8);

        //Test data
        Atom a1 = new Atom(EnumType.A);
        Atom a2 = new Atom(EnumType.A);
        Atom a3 = new Atom(EnumType.A);

        a1.bonds.add(a2);
        a2.bonds.add(a1);

        a2.bonds.add(a3);
        a3.bonds.add(a2);

        map.addAtom(new SquareLocation(6, 0), a1);
        map.addAtom(new SquareLocation(6, 1), a2);
        map.addAtom(new SquareLocation(6, 2), a3);

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
