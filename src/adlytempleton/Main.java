package adlytempleton;

import adlytempleton.atom.Atom;
import adlytempleton.atom.EnumType;
import adlytempleton.map.Simulator;
import adlytempleton.map.SquareLocation;
import adlytempleton.map.SquareMap;
import adlytempleton.reaction.ReactionData;

public class Main {

    public static void main(String[] args) {
        SquareMap map = new SquareMap(15);

        //Test data
        Atom a1 = new Atom(EnumType.A);
        Atom a2 = new Atom(EnumType.A);
        Atom a3 = new Atom(EnumType.A);

        map.addAtom(new SquareLocation(6, 0), a1);
        map.addAtom(new SquareLocation(6, 1), a2);
        map.addAtom(new SquareLocation(6, 2), a3);

        map.addAtom(new SquareLocation(1, 1), new Atom(EnumType.B));
        map.addAtom(new SquareLocation(1, 2), new Atom(EnumType.B));
        map.addAtom(new SquareLocation(1, 3), new Atom(EnumType.B));

        map.addAtom(new SquareLocation(15, 15), new Atom(EnumType.E, 5, new ReactionData(EnumType.X, EnumType.X, 0, 0, 1, 1, false, true)));
        Simulator simulator = new Simulator(map);

        while (true) {

            simulator.tick();

            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
