package server;

/**
 * Created by bondd on 10/30/2017.
 */


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class JsonEncoder {

    Gson gson;


    public JsonEncoder() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    //class to hold an array of strings called "data"
    class ArrayHolderClass{
        String[] data;
    }

    //class to hold an array of Locations called "data"
    class LocationHolder
    {
        RandomNames.Location[] data;
    }

    //load a list of strings from a JSON
    List<String> loadList(String filename)
    {
        try {
            String items = fileToString(filename);
            ArrayHolderClass arrayHolderClass = gson.fromJson(items, ArrayHolderClass.class);
            return Arrays.asList(arrayHolderClass.data);
        }
        catch (Exception ex)
        {
            System.out.println("ERROR READING JSON!!");
            return null;
        }
    }

    //load locations from JSON file
    List<RandomNames.Location> loadLocations(String filename)
    {
        try {
            String items = fileToString(filename);
            LocationHolder locationHolder = gson.fromJson(items, LocationHolder.class);
            return Arrays.asList(locationHolder.data);
        }
        catch (Exception ex)
        {
            System.out.println("ERROR READING JSON!!");
            return null;
        }
    }

    static String fileToString(String filename) throws Exception {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }
}
