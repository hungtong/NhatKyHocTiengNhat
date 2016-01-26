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
import app.learning.fantaster.nhatkyhoctiengnhat.activity.NewExample;
import app.learning.fantaster.nhatkyhoctiengnhat.adapter.UserExampleAdapter;
import app.learning.fantaster.nhatkyhoctiengnhat.data.Clause;

public class UserExamplesFragment extends Fragment {

    private RecyclerView exampleSection;
    private ArrayList<String> examples;
    private UserExampleAdapter exampleAdapter;

    private TextView numberOfExamples;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_examples, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Clause clauseSelected = ((DetailedClause) getActivity()).getClauseSelected();

        examples = new ArrayList<>(clauseSelected.example);
        numberOfExamples = (TextView) view.findViewById(R.id.number_of_examples);
        numberOfExamples.setText(
                String.format(getString(R.string.number_of_user_examples), examples.size())
        );
        exampleSection = (RecyclerView) view.findViewById(R.id.user_examples_list);
        exampleAdapter = new UserExampleAdapter(getActivity(), examples, new UserExampleAdapter.UserExampleListener() {
            @Override
            public void onDelete(int position) {
                deleteExample(position);
            }
            @Override
            public void onModify(int position) {
               modifyExample(position);
            }
        });
        RecyclerView.LayoutManager exampleLayoutManager = new LinearLayoutManager(getActivity());
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

    private void deleteExample(int position) {
        ((DetailedClause) getActivity()).getClauseDAO().deleteExample(examples.get(position));
        examples.remove(position);
        exampleAdapter.notifyDataSetChanged();
        numberOfExamples.setText(
                String.format(getString(R.string.number_of_user_examples), examples.size())
        );
    }

    private void modifyExample(int position) {
        Intent intent = new Intent(getActivity(), AddOnModification.class);
        intent.putExtra(DetailedClause.KEY_GET_CONTENT_TO_MODIFY, examples.get(position));
        intent.putExtra(DetailedClause.KEY_GET_WHAT_TO_MODIFY, getString(R.string.user_example_tab));
        ((DetailedClause) getActivity()).exampleIsModified(true);
        ((DetailedClause) getActivity()).setPositionModified(position);
        getActivity().startActivityForResult(intent, DetailedClause.REQUEST_CODE_MODIFY);
    }

    public ArrayList<String> getExamples() {
        return examples;
    }

    public UserExampleAdapter getExampleAdapter() {
        return exampleAdapter;
    }

    public TextView getTotalExamplesInfo() {
        return numberOfExamples;
    }

}
