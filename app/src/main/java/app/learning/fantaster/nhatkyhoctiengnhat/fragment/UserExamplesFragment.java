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
import app.learning.fantaster.nhatkyhoctiengnhat.activity.NewExample;
import app.learning.fantaster.nhatkyhoctiengnhat.adapter.UserAddOnAdapter;
import app.learning.fantaster.nhatkyhoctiengnhat.data.Clause;

public class UserExamplesFragment extends Fragment {

    private RecyclerView exampleSection;
    private ArrayList<String> examples;
    private UserAddOnAdapter exampleAdapter;

    private TextView numberOfExamples;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_examples, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Clause clauseSelected = ((DetailedClause) getActivity()).getClauseSelected();

        exampleSection = (RecyclerView) view.findViewById(R.id.user_examples_list);
        examples = new ArrayList<>(clauseSelected.example);
        exampleAdapter = new UserAddOnAdapter(getActivity(), examples, new UserAddOnAdapter.UserAddOnListener() {
            @Override
            public void onDelete(int position) {
                ((DetailedClause) getActivity()).getClauseDAO().deleteMemoryTrick(examples.get(position));
                examples.remove(position);
                exampleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onModify(int position) {

            }
        });
        RecyclerView.LayoutManager exampleLayoutManager = new LinearLayoutManager(getActivity());
        numberOfExamples = (TextView) view.findViewById(R.id.number_of_examples);
        numberOfExamples.setText(
                String.format(getString(R.string.number_of_memory_tricks), examples.size())
        );
        exampleSection.setAdapter(exampleAdapter);
        exampleSection.setLayoutManager(exampleLayoutManager);


        view.findViewById(R.id.add_example_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewExample.class);
                getActivity().startActivityForResult(intent, DetailedClause.REQUEST_CODE_EXAMPLE);
            }
        });
    }

    public ArrayList<String> getExamples() {
        return examples;
    }

    public UserAddOnAdapter getExampleAdapter() {
        return exampleAdapter;
    }

    public TextView getTotalExamplesInfo() {
        return numberOfExamples;
    }

}
