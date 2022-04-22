package activity;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import mainactivity.*;


public class EventActivity extends SingleFragmentActivity {

    private final String TAG = "EventActivity";
    public static final String EXTRA_EVENT_ID =
            "edu.byu.cs240.dallinstewart.familymapclient.eventIntent.event_id";

    @Override
    protected Fragment createFragment() {
        //Event activity just creates a map fragment
        String eventID = getIntent().getStringExtra(EXTRA_EVENT_ID);
        return new MapsFragment(eventID);
    }

    public static Intent newIntent(Context packageContext, String eventID) {
        //Handle new intents when another activity wants to call Event Activity
        String staticTAG = "EventActivity";
        Log.d(staticTAG, "newIntent called");
        Intent intent = new Intent(packageContext, EventActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, eventID);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Build the up button
        Log.d(TAG, "onOptionsItemSelected clicked");
        if(item.getItemId() == android.R.id.home) {
            Intent intent= new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }
}