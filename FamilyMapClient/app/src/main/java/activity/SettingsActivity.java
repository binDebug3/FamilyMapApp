package activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import mainactivity.MainActivity;
import edu.byu.cs240.dallinstewart.familymapclient.R;
import model.DataCache;
import model.Settings;

public class SettingsActivity extends AppCompatActivity {

    private final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //instantiate and initialize switch variables
        Switch lifeLineToggle = (Switch) findViewById(R.id.toggleLifeStoryLines);
        Switch familyLineToggle = (Switch) findViewById(R.id.toggleFamilyTreeLines);
        Switch spouseLineToggle = (Switch) findViewById(R.id.toggleSpouseLines);
        Switch fatherToggle = (Switch) findViewById(R.id.toggleFatherSide);
        Switch motherToggle = (Switch) findViewById(R.id.toggleMotherSide);
        Switch maleToggle = (Switch) findViewById(R.id.toggleMaleEvents);
        Switch femaleToggle = (Switch) findViewById(R.id.toggleFemaleEvents);
        TextView logout = (TextView) findViewById(R.id.textLogout);
        //update switches based on current settings
        lifeLineToggle.setChecked(Settings.getInstance().isLifeStoryLines());
        familyLineToggle.setChecked(Settings.getInstance().isFamilyTreeLines());
        spouseLineToggle.setChecked(Settings.getInstance().isSpouseLines());
        fatherToggle.setChecked(Settings.getInstance().isFatherSide());
        motherToggle.setChecked(Settings.getInstance().isMotherSide());
        maleToggle.setChecked(Settings.getInstance().isMaleEvents());
        femaleToggle.setChecked(Settings.getInstance().isFemaleEvents());

        //set a listener to make the switches clickable and update settings appropriately
        lifeLineToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "lifeLineToggle clicked");
                boolean toggleChecked = lifeLineToggle.isChecked();
                lifeLineToggle.setChecked(toggleChecked);
                Settings.getInstance().setLifeStoryLines(toggleChecked);
            }
        });
        familyLineToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "familyLineToggle clicked");
                boolean toggleChecked = familyLineToggle.isChecked();
                familyLineToggle.setChecked(toggleChecked);
                Settings.getInstance().setFamilyTreeLines(toggleChecked);
            }
        });
        spouseLineToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "spouseLineToggle clicked");
                boolean toggleChecked = spouseLineToggle.isChecked();
                spouseLineToggle.setChecked(toggleChecked);
                Settings.getInstance().setSpouseLines(toggleChecked);
            }
        });
        fatherToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "fatherToggle clicked");
                boolean toggleChecked = fatherToggle.isChecked();
                fatherToggle.setChecked(toggleChecked);
                Settings.getInstance().setFatherSide(toggleChecked);
                DataCache.getInstance().updateDisplayEvents();
                DataCache.getInstance().setEventsUpdated(true);
            }
        });
        motherToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "motherToggle clicked");
                boolean toggleChecked = motherToggle.isChecked();
                motherToggle.setChecked(toggleChecked);
                Settings.getInstance().setMotherSide(toggleChecked);
                DataCache.getInstance().updateDisplayEvents();
                DataCache.getInstance().setEventsUpdated(true);
            }
        });
        maleToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "maleToggle clicked");
                boolean toggleChecked = maleToggle.isChecked();
                maleToggle.setChecked(toggleChecked);
                Settings.getInstance().setMaleEvents(toggleChecked);
                DataCache.getInstance().updateDisplayEvents();
                DataCache.getInstance().setEventsUpdated(true);
            }
        });
        femaleToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "femaleToggle clicked");
                boolean toggleChecked = femaleToggle.isChecked();
                femaleToggle.setChecked(toggleChecked);
                Settings.getInstance().setFemaleEvents(toggleChecked);
                DataCache.getInstance().updateDisplayEvents();
                DataCache.getInstance().setEventsUpdated(true);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "logout clicked");
                DataCache.getInstance().clear();
                Intent intent = MainActivity.newIntent(SettingsActivity.this);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Build the up button
        Log.d(TAG, "onOptionsItemSelected called");
        if(item.getItemId() == android.R.id.home) {
            Intent intent= new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }

    public static Intent newIntent(Context packageContext) {
        //Handle new intents when another activity wants to call Settings Activity
        String staticTAG = "SettingsActivity";
        Log.d(staticTAG, "newIntent called");
        return new Intent(packageContext, SettingsActivity.class);
    }
}