package app.learning.fantaster.nhatkyhoctiengnhat.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.learning.fantaster.nhatkyhoctiengnhat.R;
import app.learning.fantaster.nhatkyhoctiengnhat.data.ClauseCardContent;
import app.learning.fantaster.nhatkyhoctiengnhat.util.listener.ClauseListener;

public class ClauseAdapter extends RecyclerView.Adapter<ClauseAdapter.ClauseViewHolder> {

    private Activity context;
    private ArrayList<ClauseCardContent> list;
    private static ClauseListener listener;

    public ClauseAdapter(Activity context, ArrayList<ClauseCardContent> list,
                         ClauseListener concreteListener) {
        this.context = context;
        this.list = list;
        listener = concreteListener;
    }

    public static class ClauseViewHolder extends RecyclerView.ViewHolder
                                        implements View.OnClickListener, View.OnLongClickListener {


        public ClauseViewHolder(View view) {
            super(view);

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

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
