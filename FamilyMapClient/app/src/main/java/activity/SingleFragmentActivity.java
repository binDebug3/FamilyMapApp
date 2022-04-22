package activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import mainactivity.MapsFragment;
import edu.byu.cs240.dallinstewart.familymapclient.R;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    //abstract class to reduce duplicated code while creating fragments
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            if (fragment instanceof MapsFragment)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
