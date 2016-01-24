package app.learning.fantaster.nhatkyhoctiengnhat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.Date;

import app.learning.fantaster.nhatkyhoctiengnhat.R;
import app.learning.fantaster.nhatkyhoctiengnhat.data.Clause;
import app.learning.fantaster.nhatkyhoctiengnhat.database.clause.ClauseDAO;
import app.learning.fantaster.nhatkyhoctiengnhat.database.clause.ClauseDatabaseHelper;
import app.learning.fantaster.nhatkyhoctiengnhat.fragment.ClauseDescriptionFragment;
import app.learning.fantaster.nhatkyhoctiengnhat.fragment.ClauseTabFragment;
import app.learning.fantaster.nhatkyhoctiengnhat.fragment.UserExamplesFragment;
import app.learning.fantaster.nhatkyhoctiengnhat.fragment.UserMemoryTricksFragment;

public class DetailedClause extends AppCompatActivity {

    public static final String KEY_GET_UPDATED_LAST_EXAMPLE_ON =  "key to get updated last example on";

    public static final int REQUEST_CODE_EXAMPLE = 111;
    public static final int REQUEST_CODE_MEMORY_TRICK = 112;
    public static final int RESULT_CODE_OK = 970;

    private ClauseDescriptionFragment clauseDescriptionFragment;
    private UserExamplesFragment userExamplesFragment;
    private UserMemoryTricksFragment userMemoryTricksFragment;
    private Clause clauseSelected;
    private ClauseDAO dao;
    private boolean newExampleAdded;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detailed_clause);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialize();
    }

    private void initialize() {
        clauseDescriptionFragment = new ClauseDescriptionFragment();
        userExamplesFragment = new UserExamplesFragment();
        userMemoryTricksFragment = new UserMemoryTricksFragment();
        clauseSelected = getIntent().getParcelableExtra(ClauseTabFragment.KEY_GET_CURRENT_CLAUSE);
        dao = new ClauseDAO(ClauseDatabaseHelper.getInstance(getApplicationContext()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.detailed_tab);
        ViewPager viewPager = (ViewPager) findViewById(R.id.detailed_tab_view_pager);
        viewPager.setAdapter(new DetailedPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

    }

    public Clause getClauseSelected() {
        return clauseSelected;
    }

    public ClauseDAO getClauseDAO() {
        return dao;
    }

    class DetailedPagerAdapter extends FragmentPagerAdapter {
        public static final int SO_LUONG_TAB = 3;

        public DetailedPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0 :
                    return clauseDescriptionFragment;
                case 1 :
                    return userExamplesFragment;
                default:
                    return userMemoryTricksFragment;
            }

        }

        @Override
        public int getCount() {
            return SO_LUONG_TAB;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            CharSequence title = "";
            switch (position) {
                case 0 :
                    title = getString(R.string.clause_description);
                    break;
                case 1 :
                    title = getString(R.string.user_example_tab);
                    break;
                case 2 :
                    title = getString(R.string.user_memory_trick);
                    break;
            }
            return title;
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DetailedClause.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        String lastExampleAddedOn = "";
        if (newExampleAdded) {     // To make sure this change will be visible immediately
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mma EEEE, d MMMM yyyy");
            lastExampleAddedOn = simpleDateFormat.format(new Date());
        }
        intent.putExtra(KEY_GET_UPDATED_LAST_EXAMPLE_ON, lastExampleAddedOn);
        setResult(ClauseTabFragment.RESULT_CODE_OK, intent);
        super.onBackPressed(); // must happen after setResult
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CODE_OK && data != null) {
            switch (requestCode) {
                case REQUEST_CODE_EXAMPLE :
                    String newExample = data.getStringExtra(NewExample.KEY_GET_NEW_EXAMPLE);
                    if (newExample != null && !newExample.equals("")) {
                        clauseSelected.example.add(newExample);     // this is for database, new row is added at the end
                        userExamplesFragment.getExamples().add(0, newExample);  // this is for displaying, new member is add on top
                        userExamplesFragment.getExampleAdapter().notifyDataSetChanged();
                        userExamplesFragment.getTotalExamplesInfo().setText(
                                String.format(getString(R.string.number_of_user_examples), userExamplesFragment.getExamples().size())
                        );
                        newExampleAdded = true;
                        dao.addExample(clauseSelected);
                    }
                    break;

                case REQUEST_CODE_MEMORY_TRICK :
                    String newMemoryTrick = data.getStringExtra(NewMemoryTrick.KEY_GET_NEW_MEMORY_TRICK);
                    if (newMemoryTrick != null && !newMemoryTrick.equals("")) {
                        clauseSelected.memoryTrick.add(newMemoryTrick); // this is for database, new row is added at the end
                        userMemoryTricksFragment.getMemoryTricks().add(0, newMemoryTrick);    // this is for displaying, new member is add on top
                        userMemoryTricksFragment.getMemoryTrickAdapter().notifyDataSetChanged();
                        userMemoryTricksFragment.getTotalMemoryTricksInfo().setText(String.format(getString(R.string.number_of_memory_tricks), userMemoryTricksFragment.getMemoryTricks().size()));
                        dao.addMemoryTrick(clauseSelected);
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
