package app.learning.fantaster.nhatkyhoctiengnhat.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.learning.fantaster.nhatkyhoctiengnhat.R;
import app.learning.fantaster.nhatkyhoctiengnhat.data.Clause;
import app.learning.fantaster.nhatkyhoctiengnhat.util.listener.ClauseListener;

public class ClauseAdapter extends RecyclerView.Adapter<ClauseAdapter.ClauseViewHolder> {

    private Activity context;
    private ArrayList<Clause> list;
    private static ClauseListener listener;

    public ClauseAdapter(Activity context, ArrayList<Clause> list,
                         ClauseListener concreteListener) {
        this.context = context;
        this.list = list;
        listener = concreteListener;
    }

    public static class ClauseViewHolder extends RecyclerView.ViewHolder
                                        implements View.OnClickListener, View.OnLongClickListener {

        public final TextView clause, formula, briefSummary, explanation, example, topic, lastExampleOn;

        public ClauseViewHolder(View view) {
            super(view);

            clause = (TextView) view.findViewById(R.id.clause);
            formula = (TextView) view.findViewById(R.id.formula);
            briefSummary = (TextView) view.findViewById(R.id.brief_summary);
            explanation = (TextView) view.findViewById(R.id.explanation);
            example = (TextView) view.findViewById(R.id.example);
            topic = (TextView) view.findViewById(R.id.topic);
            lastExampleOn = (TextView) view.findViewById(R.id.lastExampleOn);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onCloserView(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onDelete(view, getAdapterPosition());
            return true;
        }
    }

    @Override
    public ClauseViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
        View view = context.getLayoutInflater().inflate(R.layout.clause_card, container, false);
        return new ClauseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClauseViewHolder viewHolder, int position) {
        Clause currentClause = list.get(position);

        viewHolder.clause.setText(currentClause.clause);
        viewHolder.formula.setText(currentClause.formula);
        viewHolder.briefSummary.setText(currentClause.briefSummary);
        viewHolder.explanation.setText(currentClause.explanation);
        viewHolder.example.setText(currentClause.example);
        viewHolder.topic.setText(currentClause.topic);
        viewHolder.lastExampleOn.setText(currentClause.lastExampleOn);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
