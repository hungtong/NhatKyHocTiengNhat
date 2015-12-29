package app.learning.fantaster.nhatkyhoctiengnhat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import app.learning.fantaster.nhatkyhoctiengnhat.DetailedClause;
import app.learning.fantaster.nhatkyhoctiengnhat.R;
import app.learning.fantaster.nhatkyhoctiengnhat.adapter.ClauseAdapter;
import app.learning.fantaster.nhatkyhoctiengnhat.data.Clause;
import app.learning.fantaster.nhatkyhoctiengnhat.database.clause.ClauseDatabaseHelper;
import app.learning.fantaster.nhatkyhoctiengnhat.database.clause.DAOClause;
import app.learning.fantaster.nhatkyhoctiengnhat.util.listener.ClauseListener;


public class ClauseTabFragment extends Fragment {

    public static final int REQUEST_CODE = 463;
    public static final int RESULT_CODE_OK = 364;

    private ArrayList<Clause> list;
    private ClauseAdapter adapter;

    private int selectedPosition = 0;
    private ClauseDatabaseHelper databaseHelper;
    private DAOClause dao;

    public static ClauseTabFragment newInstance() {
        return new ClauseTabFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_clause_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.clause_tab_recycler_view);

        databaseHelper = new ClauseDatabaseHelper(getActivity());
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
        dao = new DAOClause(databaseHelper);
        list = dao.getAllClauses();
        adapter = new ClauseAdapter(getActivity(), list, new OnConcreteListener());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.topic_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        view.findViewById(R.id.original_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                adapter.notifyDataSetChanged();
                list.addAll(dao.getAllClauses());
                adapter.notifyDataSetChanged();
            }
        });

        view.findViewById(R.id.last_example_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int SIZE = list.size();
                Clause[] clauseArray = list.toArray(new Clause[list.size()]);
                list.clear();
                adapter.notifyDataSetChanged();
                final long[] lastExampleOn = new long[SIZE];
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mma EEEE, d MMMM yyyy");
                for (int i = 0; i < lastExampleOn.length; i++) {
                    try {
                        if (clauseArray[i].lastExampleOn == null)
                            lastExampleOn[i] = 0;
                        else lastExampleOn[i] = simpleDateFormat.parse(clauseArray[i].lastExampleOn).getTime();
                    } catch (ParseException ex) {
                        Log.d("Bad Format", "Unable to convert date to milliseconds");
                    }
                }
                quickSort(0, lastExampleOn.length - 1, lastExampleOn, clauseArray);

                list.addAll(Arrays.asList(clauseArray));
                adapter.notifyDataSetChanged();
            }
        });
    }

    class OnConcreteListener implements ClauseListener {

        @Override
        public void onCloserView(View childPressed, final int position) {
            selectedPosition = position;
            Intent intent = new Intent(getActivity(), DetailedClause.class);
            startActivityForResult(intent, REQUEST_CODE);
        }

    }

    private void quickSort(int left, int right, long[] lastExampleOn, Clause[] clauseArray) {
        if (left <= right) {
            int x = left;
            int y = right;
            long key = lastExampleOn[left + (right - left)/2];

            do {
                while (lastExampleOn[x] < key)
                    x++;
                while (lastExampleOn[y] > key)
                    y--;
                if (x <= y) {
                    if (x < y) {
                        long temp1 = lastExampleOn[x];
                        lastExampleOn[x] = lastExampleOn[y];
                        lastExampleOn[y] = temp1;

                        Clause temp2 = clauseArray[x];
                        clauseArray[x] = clauseArray[y];
                        clauseArray[y] = temp2;
                    }
                    x++;
                    y--;
                }
            } while (x <= y);
            quickSort(left, y, lastExampleOn, clauseArray);
            quickSort(x, right, lastExampleOn, clauseArray);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE_OK && data != null) {
            String updatedExample = data.getStringExtra(DetailedClause.KEY_GET_UPDATED_EXAMPLE);
            if (!updatedExample.equals("")) {
                list.get(selectedPosition).example = updatedExample;
                list.get(selectedPosition).lastExampleOn = data.getStringExtra(DetailedClause.KEY_GET_UPDATED_LAST_EXAMPLE_ON);
            }
            adapter.notifyItemChanged(selectedPosition);
        }   // Remember make changes on database as well
    }

}
