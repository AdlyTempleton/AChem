package adlytempleton.simulator;

import adlytempleton.atom.Atom;
import adlytempleton.map.ILocation;
import adlytempleton.map.SquareLocation;
import adlytempleton.map.SquareMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.TypeAdapter;
import com.google.gson.graph.GraphAdapterBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ATempleton on 11/28/2015.
 */
public class Serialization {

    public static SquareMap fromFile(String filename){
        //Creates GSON Object
        Gson gson = getGson();

        File file = new File(filename);

        FileReader fw = null;
        SquareMap map = null;
        try {
            //Read the file into one string
            String contents = String.join("\n", Files.readAllLines(file.toPath()).toArray(new String[0]));

            //Deserialize a list
            Type listType = new TypeToken<ArrayList<Atom>>() {}.getType();
            List<Atom> atoms = gson.fromJson(contents, listType);

            //Reform the map
            map = new SquareMap(SimulatorConstants.MAP_SIZE);

            for(Atom atom : atoms){
                map.addAtom(atom.getLocation(), atom);
            }

            //Update enzyme mappings
            map.updateAllEnzymes();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    private static Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        //Allows circular refrences
        new GraphAdapterBuilder().addType(Atom.class).registerOn(builder);

        //Fix interface issues
        builder.registerTypeAdapter(ILocation.class, new LocationAdapter());

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

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(gson.toJson(map.getAllAtoms()));
            bw.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class LocationAdapter extends TypeAdapter<ILocation> {

        public ILocation read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }
            String xy = reader.nextString();
            String[] parts = xy.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            return new SquareLocation(x, y);
        }

        public void write(JsonWriter writer, ILocation value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            SquareLocation sqLoc = (SquareLocation) value;
            String xy = sqLoc.getX() + "," + sqLoc.getY();
            writer.value(xy);
        }
    }

}
