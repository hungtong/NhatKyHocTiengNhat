package app.learning.fantaster.nhatkyhoctiengnhat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.learning.fantaster.nhatkyhoctiengnhat.DetailedClause;
import app.learning.fantaster.nhatkyhoctiengnhat.R;
import app.learning.fantaster.nhatkyhoctiengnhat.adapter.ClauseAdapter;
import app.learning.fantaster.nhatkyhoctiengnhat.data.Clause;
import app.learning.fantaster.nhatkyhoctiengnhat.util.listener.ClauseListener;


public class ClauseTabFragment extends Fragment {

    public static final String KEY_GET_POSITION = "key to get current position of card clicked";
    public static final int REQUEST_CODE = 463;
    public static final int RESULT_CODE_OK = 364;

    private ArrayList<Clause> list;
    private ClauseAdapter adapter;

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

        list = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            list.add(new Clause());
        adapter = new ClauseAdapter(getActivity(), list, new OnConcreteListener());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    class OnConcreteListener implements ClauseListener {

        @Override
        public void onDelete(View childPressed, final int position) {
            list.remove(position);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCloserView(View childPressed, final int position) {
            Intent intent = new Intent(getActivity(), DetailedClause.class);
            intent.putExtra(KEY_GET_POSITION, position);
            startActivityForResult(intent, REQUEST_CODE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && requestCode == RESULT_CODE_OK && data != null) {
            int currentClausePosition = data.getIntExtra(KEY_GET_POSITION, 0);
            list.get(currentClausePosition).example = data.getStringExtra(DetailedClause.KEY_GET_UPDATED_EXAMPLE);
            list.get(currentClausePosition).lastExampleOn = data.getStringExtra(DetailedClause.KEY_GET_UPDATED_LAST_EXAMPLE_ON);


            adapter.notifyItemChanged(currentClausePosition);
        }   // Remember make changes on database as well
    }

}
