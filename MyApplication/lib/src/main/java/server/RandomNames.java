package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by bondd on 10/23/2017.
 */

public class RandomNames {

    private List<String> femaleNames;
    private List<String> maleNames;
    private List<String> lastNames;
    private List<String> locations;
    private Random rand;

    public RandomNames()
    {
        loadListsFromJSON();
        rand = new Random();

    }

    //populate the lists from the provided JSON files
    void loadListsFromJSON()
    {
        //TEMP: LOADING WITH TEST DATA
        //NEEDS TO BE REPLACED WITH CODE TO READ FROM JSON
        femaleNames = Arrays.asList("Jill", "Jane", "Jenny");
        maleNames
    }

    //return a random female name
    public String getRandomFemaleName()
    {
        int index = rand.nextInt(femaleNames.size() - 1); //get a random index within the size of the list
        return femaleNames.get(index);
    }

    //return a random male name
    public String getRandomMaleName()
    {
        int index = rand.nextInt(maleNames.size() - 1); //get a random index within the size of the list
        return maleNames.get(index);
    }

    //return a random last name
    public String getRandomLastName()
    {
        int index = rand.nextInt(lastNames.size() - 1); //get a random index within the size of the list
        return lastNames.get(index);
    }

    //return a random location
    public String getRandomLocation()
    {
        int index = rand.nextInt(locations.size() - 1); //get a random index within the size of the list
        return locations.get(index);
    }


}
