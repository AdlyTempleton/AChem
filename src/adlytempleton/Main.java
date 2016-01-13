package adlytempleton;

import adlytempleton.map.Simulator;
import adlytempleton.map.SquareMap;
import adlytempleton.simulator.Serialization;
import adlytempleton.simulator.SimulatorConstants;

public class Main {

    public static void main(String[] args) {

        SquareMap map = Serialization.fromFile("cell.json");

        Simulator simulator = new Simulator(map);

        simulator.populateFood(map);


        int ticks = 0;

        while (true) {


            //Check if paused
            if (SimulatorConstants.simulationSpeed != -1) {
                long start = System.currentTimeMillis();
                simulator.tick();
                ticks++;

                System.out.println(ticks + " " + (System.currentTimeMillis() - start));

                if (ticks > SimulatorConstants.FLOOD_DELAY && ticks % SimulatorConstants.FLOOD_FREQUENCY == 0) {
                    simulator.flood(map);
                }


                try {
                    //Sanity check - this value is -1 if the simulation is paused

                    if (SimulatorConstants.simulationSpeed > 0) {
                        Thread.sleep(SimulatorConstants.simulationSpeed);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }
        }
    }
}

