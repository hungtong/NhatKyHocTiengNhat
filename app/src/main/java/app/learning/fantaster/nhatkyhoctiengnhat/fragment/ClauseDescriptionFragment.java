package app.learning.fantaster.nhatkyhoctiengnhat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.learning.fantaster.nhatkyhoctiengnhat.R;
import app.learning.fantaster.nhatkyhoctiengnhat.data.Clause;

public class ClauseDescriptionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_clause_description, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Clause clauseSelected = getActivity().getIntent().getParcelableExtra(ClauseTabFragment.KEY_GET_CURRENT_CLAUSE);

        ((TextView) view.findViewById(R.id.formula_section)).setText(clauseSelected.formula);
        ((TextView) view.findViewById(R.id.brief_summary_section)).setText(clauseSelected.briefSummary);
        ((TextView) view.findViewById(R.id.explanation_section)).setText(clauseSelected.explanation);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < clauseSelected.topic.size(); i++) {
            builder.append(clauseSelected.topic.get(i)).append("\n");
        }
        ((TextView) view.findViewById(R.id.topic_section)).setText(builder.toString());
    }
}
