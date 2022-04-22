package FakeData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.Reader;
import java.util.Random;

public class LocationLoader {
    private static LocationData locData;

    public LocationLoader() {
//        locData = null;
    }

    public boolean loadData() {
        boolean success = false;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            if (locData == null) {
                Reader reader = new FileReader("json/locations.json");
                locData = (LocationData) gson.fromJson(reader, LocationData.class);
            }
            success = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return success;
    }

    public Location getRandLocation() {
        Random rand = new Random();
        int index = rand.nextInt(locData.getSize());
        return locData.getData()[index];
    }
}
