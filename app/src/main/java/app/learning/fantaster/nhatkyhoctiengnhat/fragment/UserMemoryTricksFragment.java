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
import app.learning.fantaster.nhatkyhoctiengnhat.activity.AddOnModification;
import app.learning.fantaster.nhatkyhoctiengnhat.activity.DetailedClause;
import app.learning.fantaster.nhatkyhoctiengnhat.activity.NewMemoryTrick;
import app.learning.fantaster.nhatkyhoctiengnhat.adapter.UserMemoryTrickAdapter;
import app.learning.fantaster.nhatkyhoctiengnhat.data.Clause;

public class UserMemoryTricksFragment extends Fragment {

    private RecyclerView  memoryTrickSection;
    private ArrayList<String> memoryTricks;
    private UserMemoryTrickAdapter memoryTrickAdapter;

    private TextView numberOfMemoryTricks;

    private Clause clauseSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_memory_tricks, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        clauseSelected = ((DetailedClause) getActivity()).getClauseSelected();

        memoryTricks = new ArrayList<>(clauseSelected.memoryTrick);
        numberOfMemoryTricks = (TextView) view.findViewById(R.id.number_of_memory_tricks);
        numberOfMemoryTricks.setText(
                String.format(getString(R.string.number_of_memory_tricks), memoryTricks.size())
        );

        memoryTrickSection = (RecyclerView) view.findViewById(R.id.user_memory_tricks_list);
        memoryTrickAdapter = new UserMemoryTrickAdapter(getActivity(), memoryTricks, new UserMemoryTrickAdapter.UserMemoryTrickListener() {
            @Override
            public void onDelete(int position) {
               deleteMemoryTrick(position);
            }
            @Override
            public void onModify(int position) {
                modifyMemoryTrick(position);
            }
        });
        RecyclerView.LayoutManager memoryTrickLayoutManager = new LinearLayoutManager(getActivity());
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

    private void deleteMemoryTrick(int position) {
        ((DetailedClause) getActivity()).getClauseDAO().deleteMemoryTrick(memoryTricks.get(position));
        memoryTricks.remove(position);
        memoryTrickAdapter.notifyDataSetChanged();
        numberOfMemoryTricks.setText(
                String.format(getString(R.string.number_of_memory_tricks), memoryTricks.size())
        );
    }

    private void modifyMemoryTrick(int position) {
        Intent intent = new Intent(getActivity(), AddOnModification.class);
        intent.putExtra(DetailedClause.KEY_GET_CONTENT_TO_MODIFY, memoryTricks.get(position));
        intent.putExtra(DetailedClause.KEY_GET_WHAT_TO_MODIFY, getString(R.string.user_memory_trick));
        ((DetailedClause) getActivity()).memoryTrickIsModified(true);
        ((DetailedClause) getActivity()).setPositionModified(position);
        getActivity().startActivityForResult(intent, DetailedClause.REQUEST_CODE_MODIFY);
    }

    public ArrayList<String> getMemoryTricks() {
        return memoryTricks;
    }

    public UserMemoryTrickAdapter getMemoryTrickAdapter() {
        return memoryTrickAdapter;
    }

    public TextView getTotalMemoryTricksInfo() {
        return numberOfMemoryTricks;
    }

}
