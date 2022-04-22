package FakeData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.Reader;
import java.util.Random;

public class NameLoader {
    private static MaleName maleData;
    private static FemaleName femaleData;
    private static Surname surnameData;

    public NameLoader() {}

    public boolean loadData() {
        boolean success = false;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            if (maleData == null) {
                Reader reader = new FileReader("json/mnames.json");
                maleData = (MaleName) gson.fromJson(reader, MaleName.class);
            }
            if (femaleData == null) {
                Reader reader = new FileReader("json/fnames.json");
                femaleData = (FemaleName) gson.fromJson(reader, FemaleName.class);
            }
            if (surnameData == null) {
                Reader reader = new FileReader("json/snames.json");
                surnameData = (Surname) gson.fromJson(reader, Surname.class);
            }
            success = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return success;
    }

    public String getRandMale() {
        Random rand = new Random();
        int index = rand.nextInt(maleData.data.length);
        return maleData.data[index];
    }
    public String getRandFemale() {
        Random rand = new Random();
        int index = rand.nextInt(femaleData.data.length);
        return femaleData.data[index];
    }
    public String getRandSurname() {
        Random rand = new Random();
        int index = rand.nextInt(surnameData.data.length);
        return surnameData.data[index];
    }
}
