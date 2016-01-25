/*
 * Copyright 2016 Adly Templeton
 *
 * This file is part of the AChem Simulator.
 *
 * The AChem Simulator is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * The AChem Simulator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Foobar. If not, see http://www.gnu.org/licenses/.
 */

package adlytempleton;

import adlytempleton.gui.SquareMapFrame;
import adlytempleton.map.Simulator;
import adlytempleton.map.SquareMap;
import adlytempleton.monitor.Experiment;
import adlytempleton.simulator.Serialization;
import adlytempleton.simulator.SimulatorConstants;

import java.beans.Expression;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {



        if(args.length == 2){
            if(args[0].equals("-e")){
                Experiment e = new Experiment();
                try {
                    e.run(args[1]);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }else{
            SquareMap map = Serialization.fromFile("cell.json", true);

            Simulator simulator = new Simulator(map);

            simulator.populateFood(map);


            int ticks = 0;


            while (true) {


                //Check if paused
                if (SimulatorConstants.simulationSpeed != -1) {
                    long start = System.currentTimeMillis();
                    simulator.tick(ticks);
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
}

