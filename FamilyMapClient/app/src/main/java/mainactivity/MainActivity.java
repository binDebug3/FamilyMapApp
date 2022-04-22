package mainactivity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import edu.byu.cs240.dallinstewart.familymapclient.R;

public class MainActivity extends AppCompatActivity implements LoginFragment2.Listener {

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_main);
        //open the login fragment
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        LoginFragment2 loginFragment2 = (LoginFragment2) fragmentManager.findFragmentById(R.id.loginFragment2);
        if(loginFragment2 == null) {
            loginFragment2 = createFirstFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragmentFrameLayout, loginFragment2)
                    .commit();
        } else {
            if(loginFragment2 instanceof LoginFragment2) {
                ((LoginFragment2) loginFragment2).registerListener(this);
            }
        }
    }
    private LoginFragment2 createFirstFragment() {
        LoginFragment2 fragment = new LoginFragment2();
        fragment.registerListener(this);
        return fragment;
    }
    @Override
    public void notifyDone() {
        //When the login fragment is done, open up the map fragment
        Log.d(TAG, "LoginFragment notifyDone called in MainActivity");
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = new MapsFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragmentFrameLayout, fragment)
                .commit();
    }

    public static Intent newIntent(Context packageContext) {
        //Handle new intents when another activity wants to call Main Activity
        String staticTAG = "MainActivity";
        Log.d(staticTAG, "newIntent called");
        return new Intent(packageContext, MainActivity.class);
    }



    //overrides to update the log details
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}