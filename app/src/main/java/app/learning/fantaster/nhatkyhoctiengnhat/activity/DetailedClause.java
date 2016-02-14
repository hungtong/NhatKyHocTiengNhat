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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import app.learning.fantaster.nhatkyhoctiengnhat.R;
import app.learning.fantaster.nhatkyhoctiengnhat.adapter.DiaryAdapter;
import app.learning.fantaster.nhatkyhoctiengnhat.data.Clause;
import app.learning.fantaster.nhatkyhoctiengnhat.data.DateOnDiary;
import app.learning.fantaster.nhatkyhoctiengnhat.data.DiaryEntry;
import app.learning.fantaster.nhatkyhoctiengnhat.database.clause.ClauseDAO;
import app.learning.fantaster.nhatkyhoctiengnhat.database.clause.ClauseDatabaseHelper;
import app.learning.fantaster.nhatkyhoctiengnhat.fragment.ClauseDescriptionFragment;
import app.learning.fantaster.nhatkyhoctiengnhat.fragment.ClauseTabFragment;
import app.learning.fantaster.nhatkyhoctiengnhat.fragment.HomeTabFragment;
import app.learning.fantaster.nhatkyhoctiengnhat.fragment.UserExamplesFragment;
import app.learning.fantaster.nhatkyhoctiengnhat.fragment.UserMemoryTricksFragment;

public class DetailedClause extends AppCompatActivity {

    public static final String KEY_GET_UPDATED_LAST_EXAMPLE_ON =  "key to get updated last example on";
    public static final String KEY_GET_CONTENT_TO_MODIFY = "key to get content in recycler view to modify";
    public static final String KEY_GET_WHAT_TO_MODIFY = "key indicate what we are modifying";

    public static final int REQUEST_CODE_EXAMPLE = 111;
    public static final int REQUEST_CODE_MEMORY_TRICK = 112;
    public static final int REQUEST_CODE_MODIFY = 113;
    public static final int RESULT_CODE_OK = 970;

    private final String FRAGMENT_CLAUSE_DESCRIPTION_TAG = "fragment clause description tag ";
    private final String FRAGMENT_USER_EXAMPLES_TAG = "fragment user examples tag ";
    private final String FRAGMENT_USER_MEMORY_TRICKS_TAG = "fragment user memory tricks tag ";

    private ClauseDescriptionFragment clauseDescriptionFragment;
    private UserExamplesFragment userExamplesFragment;
    private UserMemoryTricksFragment userMemoryTricksFragment;
    private Clause clauseSelected;
    private ClauseDAO dao;
    private boolean newExampleAdded;
    private boolean isExampleModified;
    private boolean isMemoryTrickModified;
    private int positionModified;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detailed_clause);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialize(savedInstanceState);
    }

    private void initialize(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            clauseDescriptionFragment = new ClauseDescriptionFragment();
            userExamplesFragment = new UserExamplesFragment();
            userMemoryTricksFragment = new UserMemoryTricksFragment();
        }
        else {
            clauseDescriptionFragment = (ClauseDescriptionFragment) getSupportFragmentManager().getFragment(savedInstanceState,
                                                                    FRAGMENT_CLAUSE_DESCRIPTION_TAG + clauseSelected.clause);
            userExamplesFragment = (UserExamplesFragment) getSupportFragmentManager().getFragment(savedInstanceState,
                                                                    FRAGMENT_USER_EXAMPLES_TAG + clauseSelected.clause);
            userMemoryTricksFragment = (UserMemoryTricksFragment) getSupportFragmentManager().getFragment(savedInstanceState,
                                                                FRAGMENT_USER_MEMORY_TRICKS_TAG + clauseSelected.clause);
        }

        clauseSelected = getIntent().getParcelableExtra(ClauseTabFragment.KEY_GET_CURRENT_CLAUSE);
        dao = new ClauseDAO(ClauseDatabaseHelper.getInstance(getApplicationContext()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.detailed_tab);
        ViewPager viewPager = (ViewPager) findViewById(R.id.detailed_tab_view_pager);
        viewPager.setAdapter(new DetailedPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, FRAGMENT_CLAUSE_DESCRIPTION_TAG + clauseSelected.clause, clauseDescriptionFragment);
        getSupportFragmentManager().putFragment(outState, FRAGMENT_USER_EXAMPLES_TAG + clauseSelected.clause, userExamplesFragment);
        getSupportFragmentManager().putFragment(outState, FRAGMENT_USER_MEMORY_TRICKS_TAG + clauseSelected.clause, userMemoryTricksFragment);
    }

    public Clause getClauseSelected() {
        return clauseSelected;
    }

    public ClauseDAO getClauseDAO() {
        return dao;
    }

    public void exampleIsModified(boolean isExampleModified) {
        this.isExampleModified = isExampleModified;
    }

    public void memoryTrickIsModified(boolean isMemoryTrickModified) {
        this.isMemoryTrickModified = isMemoryTrickModified;
    }

    public void setPositionModified(int positionModified) {
        this.positionModified = positionModified;
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
                    addExample(data);
                    break;

                case REQUEST_CODE_MEMORY_TRICK :
                    addMemoryTrick(data);
                    break;

                case REQUEST_CODE_MODIFY :
                    modifyAddOn(data);
                    break;
            }
        }
    }

    private void addExample(Intent data) {
        String newExample = data.getStringExtra(NewExample.KEY_GET_NEW_EXAMPLE);
        if (newExample != null && !newExample.equals("")) {
            clauseSelected.example.add(newExample);     // this is for database, new row is added at the end
            userExamplesFragment.getExamples().add(0, newExample);  // this is for displaying, new member is add on top
            userExamplesFragment.getExampleAdapter().notifyDataSetChanged();
            userExamplesFragment.getTotalExamplesInfo().setText(
                    String.format(getString(R.string.number_of_user_examples), userExamplesFragment.getExamples().size())
            );
            newExampleAdded = true;
            dao.addExample(clauseSelected.clauseId, newExample);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mma EEEE, d MMMM yyyy");
            clauseSelected.lastExampleOn = simpleDateFormat.format(new Date());
            dao.updateTableClauses(clauseSelected);

            writeDiary();
        }
    }

    /**
     * Access to instances of HomeTabFragment, datesOnDiary and diaryAdapter to add new entry
     */
    private void writeDiary() {
        HomeTabFragment homeTabFragment = HomeTabFragment.getInstance();
        ArrayList<DateOnDiary> datesOnDiary = homeTabFragment.getDatesOnDiary();
        DiaryAdapter diaryAdapter = homeTabFragment.getDiaryAdapter();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy");
        Calendar calendar = Calendar.getInstance();
        String today = simpleDateFormat.format(calendar.getTime());

        DiaryEntry newDiaryEntry = createNewEntry(today);

        if (!datesOnDiary.isEmpty()) {
            if (today.equals(datesOnDiary.get(0))) {
                datesOnDiary.get(0).dateEntries.add(newDiaryEntry);
                diaryAdapter.notifyItemChanged(0);
                homeTabFragment.getDiaryDAO().addDiaryEntry(newDiaryEntry);
            }
            else {
                ArrayList<DiaryEntry> oneEntry = new ArrayList<>();
                oneEntry.add(newDiaryEntry);

                DateOnDiary newDate = new DateOnDiary(today, oneEntry);
                datesOnDiary.add(0, newDate);

                diaryAdapter.notifyDataSetChanged();
                homeTabFragment.getDiaryDAO().addDiaryEntry(newDiaryEntry);
            }
        }

    }

    /**
     * Create a new entry with today's date
     * @param today today's date
     * @return a new entry with information about clauseId, clauseName, clause's topics, recentStudy,
     *          nextStudy (temporarily empty),
     */
    private DiaryEntry createNewEntry(String today) {
        String topics = "";
        String recentStudy = "";
        String nextStudy = "";
        for (int i = 0; i < clauseSelected.topic.size(); i++)
            topics += clauseSelected.topic.get(i);

        topics += String.format(getString(R.string.entry_topic), topics);
        recentStudy = String.format(getString(R.string.entry_recent_study), today);
        nextStudy = String.format(getString(R.string.entry_next_study), nextStudy);


        DiaryEntry newEntry = new DiaryEntry(clauseSelected.clauseId, clauseSelected.clause, topics, recentStudy, nextStudy);
        return newEntry;
    }

    private void addMemoryTrick(Intent data) {
        String newMemoryTrick = data.getStringExtra(NewMemoryTrick.KEY_GET_NEW_MEMORY_TRICK);
        if (newMemoryTrick != null && !newMemoryTrick.equals("")) {
            clauseSelected.memoryTrick.add(newMemoryTrick); // this is for database, new row is added at the end
            userMemoryTricksFragment.getMemoryTricks().add(0, newMemoryTrick);    // this is for displaying, new member is add on top
            userMemoryTricksFragment.getMemoryTrickAdapter().notifyDataSetChanged();
            userMemoryTricksFragment.getTotalMemoryTricksInfo().setText(String.format(getString(R.string.number_of_memory_tricks), userMemoryTricksFragment.getMemoryTricks().size()));
            dao.addMemoryTrick(clauseSelected.clauseId, newMemoryTrick);
        }
    }

    private void modifyAddOn(Intent data) {
        String modifiedContent = data.getStringExtra(AddOnModification.KEY_TO_GET_MODIFIED_CONTENT);
        if (isExampleModified) {
            String originalExample =  clauseSelected.example.get(positionModified);
            clauseSelected.example.set(positionModified, modifiedContent);
            userExamplesFragment.getExamples().set(positionModified, modifiedContent);
            userExamplesFragment.getExampleAdapter().notifyItemChanged(positionModified);
            dao.updateTableExamples(originalExample, modifiedContent);
            isExampleModified = false;
        }
        if (isMemoryTrickModified) {
            String originalMemoryTrick = clauseSelected.memoryTrick.get(positionModified);
            clauseSelected.memoryTrick.set(positionModified, modifiedContent);
            userMemoryTricksFragment.getMemoryTricks().set(positionModified, modifiedContent);
            userMemoryTricksFragment.getMemoryTrickAdapter().notifyItemChanged(positionModified);
            dao.updateTableMemoryTricks(originalMemoryTrick, modifiedContent);
            isMemoryTrickModified = false;
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
