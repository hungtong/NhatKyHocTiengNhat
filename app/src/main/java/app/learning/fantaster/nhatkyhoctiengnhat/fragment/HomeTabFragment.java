package app.learning.fantaster.nhatkyhoctiengnhat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import app.learning.fantaster.nhatkyhoctiengnhat.R;
import app.learning.fantaster.nhatkyhoctiengnhat.adapter.DiaryAdapter;
import app.learning.fantaster.nhatkyhoctiengnhat.data.DateOnDiary;
import app.learning.fantaster.nhatkyhoctiengnhat.database.diary.DiaryDatabaseHelper;

public class HomeTabFragment extends Fragment {

    private RecyclerView diary;
    private ArrayList<DateOnDiary> datesOnDiary;

    private static HomeTabFragment instanceHomeTabFragment;

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
       intializeData();

    }

    private void intializeData() {
        DiaryDatabaseHelper databaseHelper = DiaryDatabaseHelper.getInstance(getActivity());
        try {
            databaseHelper.createDatabase();
        } catch (IOException ex) {
            Log.d("Failed", "Failed to create database");
        }
        try {
            databaseHelper.openDatabase();
        } catch (SQLException ex) {
            Log.d("Failed", "Failed to open database");
        }
    }

    private void initializeDiary(View view) {
        diary = (RecyclerView) view.findViewById(R.id.diary_of_japanese_study);
        DiaryAdapter diaryAdapter = new DiaryAdapter(getActivity(), datesOnDiary);
        diary.setAdapter(diaryAdapter);
    }

}