package adlytempleton;

import adlytempleton.atom.Atom;
import adlytempleton.atom.EnumType;
import adlytempleton.map.Simulator;
import adlytempleton.map.SquareLocation;
import adlytempleton.map.SquareMap;
import adlytempleton.reaction.ReactionData;
import adlytempleton.simulator.Serialization;
import adlytempleton.simulator.SimulatorConstants;

public class Main {

    public static void main(String[] args) {



        /**
        SquareMap map = new SquareMap(SimulatorConstants.MAP_SIZE);

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


         Serialization.toFile("maps/state.json", map);
         **/


        SquareMap map = Serialization.fromFile("cell.json");

        Simulator simulator = new Simulator(map);

        simulator.populateFood(map);

        while (true) {

            for(int i = 0; i < 100; i++){

                //Check if paused
                if(SimulatorConstants.simulationSpeed != -1) {
                    long start = System.currentTimeMillis();
                    simulator.tick();
                    System.out.println(System.currentTimeMillis() - start);
                }

                try {
                    //Sanity check - this value is -1 if the simulation is paused

                    if(SimulatorConstants.simulationSpeed >= 0) {
                        Thread.sleep(SimulatorConstants.simulationSpeed);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }

            Serialization.toFile("maps/state.json", map);
        }
    }
}
