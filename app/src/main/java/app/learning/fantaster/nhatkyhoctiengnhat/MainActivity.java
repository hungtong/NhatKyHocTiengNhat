package app.learning.fantaster.nhatkyhoctiengnhat;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.bo.nhatkyhoctiengnhat.R;

import java.util.Calendar;

import app.learning.fantaster.nhatkyhoctiengnhat.data.Date;
import app.learning.fantaster.nhatkyhoctiengnhat.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {

    /**
     * This variable will indicate specifically day, month, year stored of previous app's operation.
     * It will specified by onResume and stored by OnPause
     * Android's Life Cycler : onCreate ---> onStart() ---> onResume. Basically, no matter either user
     * suddenly escape application or start application after it completely stopped, onResume is still called
     * this is the opportunity for use to retrieve important data
     * This case used Shared Preferences, other choices are File, Database or Content Provider
     */
    public static Date STORED_DATE = new Date();

    // A Signal to Update Recycler View
    // This variable will be set false in onActivityResult in HomeTabFragment to indicate that
    // "Oh RecyclerView is already updated! Do not do it again until tomorrow"
    public static boolean signalToUpdateRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            HomeFragment fragment = new HomeFragment();
            transaction.replace(R.id.replaced_by_home_fragment, fragment);
            transaction.commit();
        }
    }

    /**
     *  Store current day
     *  Write to Shared Preferences
     */
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences pre =getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putInt(getString(R.string.current_day), STORED_DATE.getDay());
        editor.putInt(getString(R.string.current_month), STORED_DATE.getMonth());
        editor.putInt(getString(R.string.current_year), STORED_DATE.getYear());

        editor.commit();

    }

    /**
     * Retrieve day, month, and year stored. Compare those to real day, month, and year so that we can
     * change Current Today ---> Yesterday
     * Read from Shared Preferences
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Read from Shared Preferences to get day, month, and year stored
        SharedPreferences pre = getPreferences(Context.MODE_PRIVATE);
        int default_value = 0;
        int day = pre.getInt(getString(R.string.current_day), default_value);
        int month = pre.getInt(getString(R.string.current_month), default_value);
        int year = pre.getInt(getString(R.string.current_year), default_value);

        // Initially, STORED_DATE is null so these actions consider their initializations
        // Since we have default_value, the issue that STORED_DATE is terribly null may not happen
        STORED_DATE.setDay(day);
        STORED_DATE.setMonth(month);
        STORED_DATE.setYear(year);

        // Use Calendar class to get real day, month, and year to compare
        final Calendar calendar = Calendar.getInstance();
        int real_day = calendar.get(Calendar.DAY_OF_MONTH);
        int real_month = calendar.get(Calendar.MONTH);
        int real_year = calendar.get(Calendar.YEAR);

        if (day != real_day || month != real_month || year != real_year) {
            STORED_DATE.setDay(real_day);
            STORED_DATE.setMonth(real_month);
            STORED_DATE.setYear(real_year);

            // Yes recycler view needs updating
            // onActivityResult in HomeTabFragment should execute updateRecyclerView() after receiving this signal
            signalToUpdateRecyclerView = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.search);
        item.getIcon().setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.colorAccent), PorterDuff.Mode.MULTIPLY);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
     //   int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}
