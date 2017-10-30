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
    private List<Location> locations;
    private Random rand;

    private static RandomNames instance = null;

    private RandomNames() //SINGLETON
    {
        loadListsFromJSON();
        rand = new Random();
    }

    public static RandomNames getInstance()
    {
        if (instance == null)
            instance = new RandomNames();
        return instance;
    }

    //populate the lists from the provided JSON files
    private void loadListsFromJSON()
    {
        JsonEncoder json = new JsonEncoder();

        femaleNames = json.loadList("json/fnames.json");
        maleNames = json.loadList("json/mnames.json");
        lastNames = json.loadList("json/snames.json");
        locations = json.loadLocations("json/locations.json");
    }

    //return a random female name
    public String getRandomFemaleName()
    {
        int index = rand.nextInt(femaleNames.size()); //get a random index within the size of the list
        return femaleNames.get(index);
    }

    //return a random male name
    public String getRandomMaleName()
    {
        int index = rand.nextInt(maleNames.size()); //get a random index within the size of the list
        return maleNames.get(index);
    }

    //return a random last name
    public String getRandomLastName()
    {
        int index = rand.nextInt(lastNames.size()); //get a random index within the size of the list
        return lastNames.get(index);
    }

    //return a random location
    public Location getRandomLocation()
    {
        int index = rand.nextInt(locations.size()); //get a random index within the size of the list
        return locations.get(index);
    }

    public static class Location
    {
        private String country;
        private String city;
        private double latitude;
        private double longitude;

        public Location(String country, String city, double latitude, double longitude) {
            this.country = country;
            this.city = city;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }



}
