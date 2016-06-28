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

package adlytempleton.monitor;

import adlytempleton.map.AbstractMap;
import adlytempleton.map.Simulator;
import adlytempleton.map.SquareMap;
import adlytempleton.reaction.ReactionData;
import adlytempleton.simulator.Serialization;
import adlytempleton.simulator.SimulatorConstants;
import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

/**
 * Created by ATempleton on 1/24/2016.
 * <p>
 * Runs experiments through .properties files and a CLI
 */
public class Experiment {

    public int maxGenerations = 100000;

    public String baseFilename = "experiment1";
    public String numberedFilename = baseFilename;

    //Filename of the state to be loaded (ie. cell.json)
    public String state = "cell.json";

    public int repetitions = 1;

    public AbstractMap map;
    public Simulator simulator;
    public boolean instrument = false;

    public boolean showGUI = true;

    public void run(String filename) throws IOException {
        load(filename);

        for(int i = 0; i < repetitions; i++) {
            numberedFilename = baseFilename + i;
            initalizeMap();
            simulate();
        }
    }

    private void write() {
    }

    private void simulate() {
        for (int ticks = 0; ticks < maxGenerations; ticks++) {

            long start = System.currentTimeMillis();
            simulator.tick(ticks);

            if (instrument) {
                System.out.println(ticks + " " + (System.currentTimeMillis() - start));
            }

            if (ticks % 10000 == 0) {


                try {
                    snapshotBiomass(ticks);
                    snapshot(ticks);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {
                if (ticks % 1000 == 0) {


                    try {
                        snapshotBiomass(ticks);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }


            if (ticks > SimulatorConstants.FLOOD_DELAY && ticks % SimulatorConstants.FLOOD_FREQUENCY == 0) {
                simulator.flood(map);
            }

        }
    }

    private void snapshotBiomass(int ticks) throws IOException{

        File folder = new File(String.format("%s/%s/", System.getProperty("user.dir"), numberedFilename));
        folder.mkdirs();

        Path biomassPath = Paths.get(String.format("%s/%s/biomass.json", System.getProperty("user.dir"), numberedFilename));
        Files.write(biomassPath, String.format("%d:%f\n", ticks, Biomass.calculateBiomassPercentage(map)).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    private void snapshot(int ticks) throws IOException {

        //Make folders
        File folder = new File(String.format("%s/%s/states/", System.getProperty("user.dir"), numberedFilename));
        folder.mkdirs();

        folder = new File(String.format("%s/%s/enzymes/", System.getProperty("user.dir"), numberedFilename));
        folder.mkdirs();
        //folder = new File(String.format("%s/%s/reactionStats/", System.getProperty("user.dir"), numberedFilename));
        //folder.mkdirs();

        Gson gson = Serialization.getGson();

        Path enzymePath = Paths.get(String.format("%s/%s/enzymes/%d.json", System.getProperty("user.dir"), numberedFilename, ticks));
        Path statePath = Paths.get(String.format("%s/%s/states/%d.json", System.getProperty("user.dir"), numberedFilename, ticks));

        Files.deleteIfExists(enzymePath);
        Files.deleteIfExists(statePath);

        /*

        for(ReactionData data : EventTracker.monitoredReactions){
            Path reactionPath = Paths.get(String.format("%s/%s/states/%s.json", System.getProperty("user.dir"), numberedFilename, data));
            Files.write(reactionPath, String.format("%d:%d", ticks, EventTracker.eventsWithinPeriod(data, ticks - 10000, ticks)).getBytes(),StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
        */

        Serialization.toFile(statePath.toString(), (SquareMap) map);
        Files.write(enzymePath, gson.toJson(EnzymeMonitor.getNewReactions(map)).toString().getBytes(), StandardOpenOption.CREATE);

    }

    private void load(String filename) throws IOException {
        //Load constants

        Properties prop = new Properties();
        InputStream inputStream = new FileInputStream(filename);

        if (inputStream != null) {
            prop.load(inputStream);

            //All properties are optional, and will reset to default values

            if (prop.containsKey("filename")) {
                this.baseFilename = prop.getProperty("filename");
            }

            if (prop.containsKey("state")) {
                state = prop.getProperty("state");
            }

            if (prop.containsKey("repetitions")) {
                repetitions = Integer.parseInt(prop.getProperty("repetitions"));
            }

            if (prop.containsKey("instrument")) {
                instrument = Boolean.parseBoolean(prop.getProperty("instrument"));
            }

            if (prop.containsKey("maxGenerations")) {
                maxGenerations = Integer.parseInt(prop.getProperty("maxGenerations"));
            }

            if (prop.containsKey("mapSize")) {
                SimulatorConstants.MAP_SIZE = Integer.parseInt(prop.getProperty("mapSize"));
            }

            if (prop.containsKey("mutationChance")) {
                SimulatorConstants.MUTATION_CHANCE = Float.parseFloat(prop.getProperty("mutationChance"));
            }
            if (prop.containsKey("floodRadius")) {
                SimulatorConstants.FLOOD_RANGE = Integer.parseInt(prop.getProperty("floodRadius"));
            }


        } else {
            throw new FileNotFoundException("Could not find experimental properties file");
        }


    }

    private void initalizeMap() {
        map = Serialization.fromFile(state, false);

        simulator = new Simulator(map);
        simulator.populateFood(map);
    }


}
