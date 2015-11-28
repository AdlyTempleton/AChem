package adlytempleton;

import adlytempleton.atom.Atom;
import adlytempleton.atom.EnumType;
import adlytempleton.map.Simulator;
import adlytempleton.map.SquareLocation;
import adlytempleton.map.SquareMap;
import adlytempleton.reaction.ReactionData;

public class Main {

    public static void main(String[] args) {
        SquareMap map = new SquareMap(10);

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

        map.addAtom(new SquareLocation(10, 10), new Atom(EnumType.E, 5, new ReactionData[]{
                new ReactionData(EnumType.X, EnumType.X, 0, 0, 1, 2, false, true, false),
                new ReactionData(EnumType.X, EnumType.Y, 2, 2, 3, 3, false, true, false),
                new ReactionData(EnumType.X, EnumType.Y, 1, 1, 4, 4, false, true, false),
                new ReactionData(EnumType.X, EnumType.Y, 4, 3, 5, 5, true, true, false),
                new ReactionData(EnumType.E, EnumType.X, 5, 0, 6, 6, false, true, true),
        }));

        Simulator simulator = new Simulator(map);

        while (true) {

            simulator.tick();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
