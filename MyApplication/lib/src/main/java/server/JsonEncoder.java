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

import server.requests.LoadRequest;
import server.requests.LoginRequest;
import server.requests.RegisterRequest;


public class JsonEncoder {

    Gson gson;


    public JsonEncoder() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    //class to hold an array of strings called "data"
   private class ArrayHolderClass{
        String[] data;
    }

    //class to hold an array of Locations called "data"
   private class LocationHolder
    {
        RandomNames.Location[] data;
    }

    //for the register handler, reads the input json and returns the register request object
    public static RegisterRequest loadRegisterRequest(String json)
    {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            RegisterRequest registerRequest = gson.fromJson(json, RegisterRequest.class);
            return registerRequest;
        }
        catch (Exception ex)
        {
            System.out.println("ERROR READING JSON!!");
            return null;
        }
    }

    //for the load handler, reads the input json and returns a loadRequest object
    public static LoadRequest loadLoadRequest(String json)
    {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            LoadRequest loadRequest = gson.fromJson(json, LoadRequest.class);
            return loadRequest;
        }
        catch (Exception ex)
        {
            System.out.println("ERROR READING JSON!!");
            return null;
        }
    }

    //for the login handler, reads the input json and returns a loginRequest object
    public static LoginRequest loadloginRequest(String json)
    {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            LoginRequest loginRequest = gson.fromJson(json, LoginRequest.class);
            return loginRequest;
        }
        catch (Exception ex)
        {
            System.out.println("ERROR READING JSON!!");
            return null;
        }
    }

    //load a list of strings from a JSON
   public static List<String> loadList(String filename)
    {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
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
    public static List<RandomNames.Location> loadLocations(String filename)
    {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
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

    //create a JSON from a data object
    public static String encodeObject(Object obj)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(obj);
    }


    static String fileToString(String filename) throws Exception {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }
}
