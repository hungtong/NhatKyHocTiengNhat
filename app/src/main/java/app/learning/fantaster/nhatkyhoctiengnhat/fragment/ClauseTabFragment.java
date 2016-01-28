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

import app.learning.fantaster.nhatkyhoctiengnhat.R;
import app.learning.fantaster.nhatkyhoctiengnhat.activity.DetailedClause;
import app.learning.fantaster.nhatkyhoctiengnhat.activity.TopicSearch;
import app.learning.fantaster.nhatkyhoctiengnhat.adapter.ClauseAdapter;
import app.learning.fantaster.nhatkyhoctiengnhat.data.Clause;
import app.learning.fantaster.nhatkyhoctiengnhat.database.clause.ClauseDAO;
import app.learning.fantaster.nhatkyhoctiengnhat.database.clause.ClauseDatabaseHelper;


public class ClauseTabFragment extends Fragment {

    public static final int REQUEST_FILL_IN_CLAUSE_CARD = 463;
    public static final int REQUEST_TOPIC_FILTER = 364;
    public static final int RESULT_CODE_OK = 364;
    public static final String KEY_GET_LIST_TOPIC = "key to get list topic";
    public static final String KEY_GET_CURRENT_CLAUSE = "key to get current clause selected";

    private static ClauseTabFragment instanceClauseTabFragment;

    private ArrayList<Clause> list;
    private ClauseAdapter adapter;

    private int selectedPosition = 0;
    private ClauseDAO dao;

    public static ClauseTabFragment getInstance() {
        if (instanceClauseTabFragment == null) {
            instanceClauseTabFragment = new ClauseTabFragment();
        }
        return instanceClauseTabFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_clause_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.clause_tab_recycler_view);

        ClauseDatabaseHelper databaseHelper = ClauseDatabaseHelper.getInstance(getActivity());
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
        dao = new ClauseDAO(databaseHelper);
        list = dao.getAllClauses();
        adapter = new ClauseAdapter(getActivity(), list, new OnConcreteListener());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.topic_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TopicSearch.class);
                intent.putStringArrayListExtra(KEY_GET_LIST_TOPIC, dao.getTopics());
                startActivityForResult(intent, REQUEST_TOPIC_FILTER);
            }
        });

        view.findViewById(R.id.original_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOriginalFilter();
            }
        });

        view.findViewById(R.id.last_example_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFilterUponExample();
            }
        });
    }

    class OnConcreteListener implements ClauseAdapter.ClauseListener {

        @Override
        public void onCloserView(View childPressed, final int position) {
            selectedPosition = position;
            Intent intent = new Intent(getActivity(), DetailedClause.class);
            intent.putExtra(KEY_GET_CURRENT_CLAUSE, list.get(position));
            startActivityForResult(intent, REQUEST_FILL_IN_CLAUSE_CARD);
        }

    }

    /**
     * This filter display the list taken from original database
     */
    private void doOriginalFilter() {
        list.clear();
        adapter.notifyDataSetChanged();
        list.addAll(dao.getAllClauses());
        adapter.notifyDataSetChanged();
    }

    /**
     * This filter arrange all items in increasing order of date adding example.
     */
    private void doFilterUponExample() {
        Clause[] clauseArray = list.toArray(new Clause[list.size()]);
        final long[] lastExampleOn = new long[list.size()];

        list.clear();
        adapter.notifyDataSetChanged();

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
        if (resultCode == RESULT_CODE_OK && data != null) {
            switch (requestCode) {
                case REQUEST_FILL_IN_CLAUSE_CARD :
                    fillInClauseCard(data);
                    break;
                case REQUEST_TOPIC_FILTER :
                    filterByTopic(data);
                    break;
            }
        }
    }

    private void fillInClauseCard(Intent data) {
        String lastExampleAddedOn = data.getStringExtra(DetailedClause.KEY_GET_UPDATED_LAST_EXAMPLE_ON);
        if (!lastExampleAddedOn.equals("")) {
            list.get(selectedPosition).lastExampleOn = data.getStringExtra(DetailedClause.KEY_GET_UPDATED_LAST_EXAMPLE_ON);
            adapter.notifyItemChanged(selectedPosition);
        }
    }

    private void filterByTopic(Intent data) {
        String desiredTopic = data.getStringExtra(TopicSearch.KEY_GET_DESIRED_TOPIC);
        ArrayList<Clause> temporaryList = new ArrayList<>();
        ArrayList<String> currentTopics = new ArrayList<>();

        // Let topics containing keyword be on the front
        for (int i = 0; i < list.size(); i++) {
            currentTopics.addAll(list.get(i).topic);
            for (int j = 0; j < currentTopics.size(); j++)
                if (currentTopics.get(j).equalsIgnoreCase(desiredTopic)) {
                    temporaryList.add(list.get(i));
                    break;
                }
            currentTopics.clear();
        }

        boolean notContainKeyword = true;
        // Let topics not containing keyword be on the back
        for (int i = 0; i < list.size(); i++) {
            currentTopics.addAll(list.get(i).topic);
            for (int j = 0; j < currentTopics.size(); j++)
                if (currentTopics.get(j).equalsIgnoreCase(desiredTopic)) {
                    notContainKeyword = false;
                    break;
                }
            if (notContainKeyword)
                temporaryList.add(list.get(i));
            notContainKeyword = true;
            currentTopics.clear();
        }


        list.clear();
        list.addAll(temporaryList);
        adapter.notifyDataSetChanged();

    }

}
