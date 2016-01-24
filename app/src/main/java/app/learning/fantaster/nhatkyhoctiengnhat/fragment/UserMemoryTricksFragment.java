package app.learning.fantaster.nhatkyhoctiengnhat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.learning.fantaster.nhatkyhoctiengnhat.R;
import app.learning.fantaster.nhatkyhoctiengnhat.activity.DetailedClause;
import app.learning.fantaster.nhatkyhoctiengnhat.activity.NewMemoryTrick;
import app.learning.fantaster.nhatkyhoctiengnhat.adapter.UserAddOnAdapter;
import app.learning.fantaster.nhatkyhoctiengnhat.data.Clause;

public class UserMemoryTricksFragment extends Fragment {

    private RecyclerView  memoryTrickSection;
    private ArrayList<String> memoryTricks;
    private UserAddOnAdapter memoryTrickAdapter;

    private TextView numberOfMemoryTricks;

    private Clause clauseSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_memory_tricks, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        clauseSelected = ((DetailedClause) getActivity()).getClauseSelected();

        memoryTrickSection = (RecyclerView) view.findViewById(R.id.user_memory_tricks_list);
        memoryTricks = new ArrayList<>(clauseSelected.memoryTrick);

        memoryTrickAdapter = new UserAddOnAdapter(getActivity(), memoryTricks, new UserAddOnAdapter.UserAddOnListener() {
            @Override
            public void onDelete(int position) {
                ((DetailedClause) getActivity()).getClauseDAO().deleteMemoryTrick(memoryTricks.get(position));
                memoryTricks.remove(position);
                memoryTrickAdapter.notifyDataSetChanged();
            }

            @Override
            public void onModify(int position) {

            }
        });
        RecyclerView.LayoutManager memoryTrickLayoutManager = new LinearLayoutManager(getActivity());
        numberOfMemoryTricks = (TextView) view.findViewById(R.id.number_of_memory_tricks);
        numberOfMemoryTricks.setText(
                String.format(getString(R.string.number_of_memory_tricks), memoryTricks.size())
        );
        memoryTrickSection.setAdapter(memoryTrickAdapter);
        memoryTrickSection.setLayoutManager(memoryTrickLayoutManager);

        view.findViewById(R.id.add_memory_trick_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewMemoryTrick.class);
                getActivity().startActivityForResult(intent, DetailedClause.REQUEST_CODE_MEMORY_TRICK);
            }
        });
    }

    public ArrayList<String> getMemoryTricks() {
        return memoryTricks;
    }

    public UserAddOnAdapter getMemoryTrickAdapter() {
        return memoryTrickAdapter;
    }

    public TextView getTotalMemoryTricksInfo() {
        return numberOfMemoryTricks;
    }

}
