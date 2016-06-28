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

package adlytempleton.simulator;

import adlytempleton.atom.Atom;
import adlytempleton.map.*;
import adlytempleton.monitor.EnzymeMonitor;
import adlytempleton.reaction.ReactionData;
import adlytempleton.reaction.ReactionDataTriple;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ATempleton on 11/28/2015.
 */
public class Serialization {

    public static SquareMap fromFile(String filename, boolean showGUI) {
        //Creates GSON Object
        Gson gson = getGson();

        File file = new File(filename);

        SquareMap map = null;
        try {
            //Read the file into one string
            List<String> var = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

            //String.join does not exist in 1.7
            String contents = Joiner.on("\n").join(var);
            contents.replaceFirst("\n", "");

            //Deserialize a list
            Type listType = new TypeToken<ArrayList<Atom>>() {
            }.getType();
            List<Atom> atoms = gson.fromJson(contents, listType);

            //Reform the map
            map = SimulatorConstants.TOROIDAL_MAP ? new ToroidalMap(SimulatorConstants.MAP_SIZE, showGUI) : new SquareMap(SimulatorConstants.MAP_SIZE, showGUI);

            for (Atom atom : atoms) {
                map.addAtom(atom.getLocation(), atom);
            }

            //Reform bond data
            for (Atom atom : map.getAllAtoms()) {
                atom.reconstructBondList(map);
            }

            //Update enzyme mappings
            map.updateAllEnzymes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        EnzymeMonitor.loadBaselineReactions(map);

        return map;
    }

    /**
     * Creates and configures a GSON object
     *
     * @return Gson object
     */
    public static Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        //Splits data across multiple lines
        builder.setPrettyPrinting();

        //Allows circular refrences
        //new GraphAdapterBuilder().addType(Atom.class).registerOn(builder);

        //Fix interface issues
        builder.registerTypeAdapter(ILocation.class, new LocationAdapter());
        //Condensed reaction notation
        builder.registerTypeAdapter(ReactionData.class, new ReactionAdapter());
        builder.registerTypeAdapter(ReactionDataTriple.class, new ReactionAdapter());

        return builder.create();
    }

    public static void toFile(String filename, SquareMap map) {
        try {

            Gson gson = getGson();

            File file = new File(filename);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            //Collapse the bond data into refrences to locations
            for (Atom atom : map.getAllAtoms()) {
                atom.updateBondLocationList();
            }

            //Write to file
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(gson.toJson(map.getAllAtoms()));
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is a helper class that reads/writes SquareLocation data
     * Both simplifies the display of location data
     * And prevents interface issues
     */
    public static class LocationAdapter extends TypeAdapter<ILocation> {

        public ILocation read(JsonReader reader) throws IOException {
            //Boilerplate
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }

            String xy = reader.nextString();
            String[] parts = xy.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            return SimulatorConstants.TOROIDAL_MAP ? new ToroidalLocation(x, y, SimulatorConstants.MAP_SIZE) : new SquareLocation(x, y);
        }

        public void write(JsonWriter writer, ILocation value) throws IOException {
            //Boilerplate
            if (value == null) {
                writer.nullValue();
                return;
            }
            SquareLocation sqLoc = (SquareLocation) value;
            String xy = sqLoc.getX() + "," + sqLoc.getY();
            writer.value(xy);
        }
    }

    /**
     * This allows a condensed notation for ReactionData
     */
    public static class ReactionAdapter extends TypeAdapter<ReactionData> {

        public ReactionData read(JsonReader reader) throws IOException {
            //Boilerplate
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }

            return ReactionData.fromString(reader.nextString());
        }

        public void write(JsonWriter writer, ReactionData value) throws IOException {
            //Boilerplate
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value.toString());
        }
    }

}
