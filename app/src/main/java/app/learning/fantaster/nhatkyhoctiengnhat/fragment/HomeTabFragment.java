package app.learning.fantaster.nhatkyhoctiengnhat.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import app.learning.fantaster.nhatkyhoctiengnhat.R;
import app.learning.fantaster.nhatkyhoctiengnhat.adapter.DiaryAdapter;
import app.learning.fantaster.nhatkyhoctiengnhat.data.DateOnDiary;
import app.learning.fantaster.nhatkyhoctiengnhat.database.diary.DiaryDAO;
import app.learning.fantaster.nhatkyhoctiengnhat.database.diary.DiaryDatabaseHelper;

public class HomeTabFragment extends Fragment {

    private static HomeTabFragment instanceHomeTabFragment;

    private DiaryDAO dao;
    private RecyclerView diary;
    private ArrayList<DateOnDiary> datesOnDiary;
    private DiaryAdapter diaryAdapter;

    public ArrayList<DateOnDiary> getDatesOnDiary() {
        return datesOnDiary;
    }

    public DiaryAdapter getDiaryAdapter() {
        return diaryAdapter;
    }

    public DiaryDAO getDiaryDAO() {
        return dao;
    }

    public static HomeTabFragment getInstance() {
        if (instanceHomeTabFragment == null) {
            instanceHomeTabFragment = new HomeTabFragment();
        }
        return instanceHomeTabFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
       initializeDatabase();
       initializeDiary(view);
       initializeSwipeToUpdate(view);
    }

    private void initializeDatabase() {
        DiaryDatabaseHelper databaseHelper = DiaryDatabaseHelper.getInstance(getActivity().getApplicationContext());
        try {
            databaseHelper.createDatabase();
            try {
                databaseHelper.openDatabase();
            } catch (SQLException ex) {
                Log.d("Failed", "Failed to open database");
            }
        } catch (IOException ex) {
            Log.d("Failed", "Failed to create database");
        }
        dao = new DiaryDAO(databaseHelper);
    }

    private void initializeDiary(View view) {
        datesOnDiary = dao.getDatesOnDiary();
        diary = (RecyclerView) view.findViewById(R.id.diary_of_japanese_study);

        diaryAdapter = new DiaryAdapter(getActivity(), datesOnDiary);
        diary.setAdapter(diaryAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        diary.setLayoutManager(layoutManager);
    }

    private void initializeSwipeToUpdate(View view) {
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_diary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                (new DiaryUpdate()).execute();
            }
        });
    }

    /**
     * In case, the app is used continuously from today to tomorrow, user can swipe down to update like
     * currentToday -> yesterday , likewise...
     */
    class DiaryUpdate extends AsyncTask<Void, Void, Void> {

        /**
         * The objective is that at 12:00pm, when user swipes down, currentToday will turn into yester, so on.
         *  Since I kinda do a small trick in onBindParentViewHolder, actual dates stored in datebase is not
         * today, yesterday, dayBefore but in explicit date format, so now I have to get actual date to compare.
         * I cannot just leave datesOnDiary.get(sth).date as today, yesterday,.. since user probably wants to swipe
         * down multiple times like crazy (Yes, I am an example). It must remain explicit date format for later comparison
         * So I use temp to back up value
         * Note that I don't need tempDayBefore since as long yesterday turns into the day before or explicit date format,
         * dayBefore automatically turns into explicit date format
         */
        @Override
        public Void doInBackground(Void ... arg) {
            if (!datesOnDiary.isEmpty()) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy");
                Calendar calendar = Calendar.getInstance();
                String actualToday = simpleDateFormat.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                String actualYesterday = simpleDateFormat.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                String actualDayBefore = simpleDateFormat.format(calendar.getTime());

                String firstItem = datesOnDiary.get(0).date;    // for sure we have one item
                if (!firstItem.equals(actualToday)) {
                    if (firstItem.equals(actualYesterday))
                        datesOnDiary.get(0).date = getString(R.string.yesterday);
                    if (firstItem.equals(actualDayBefore))
                        datesOnDiary.get(0).date = getString(R.string.the_day_before);
                    diaryAdapter.notifyItemChanged(0);
                    datesOnDiary.get(0).date = firstItem;
                }
                if (datesOnDiary.size() >= 2) {
                    String secondItem = datesOnDiary.get(1).date;

                    if (!secondItem.equals(actualYesterday)) {
                        if (secondItem.equals(actualDayBefore))
                            datesOnDiary.get(1).date = getString(R.string.the_day_before);
                        diaryAdapter.notifyItemChanged(1);
                        if (datesOnDiary.size() >= 3) {
                            diaryAdapter.notifyItemChanged(2);
                        }
                        datesOnDiary.get(1).date = secondItem;
                    }
                }
            }
            return null;
        }
    }
}