package app.learning.fantaster.nhatkyhoctiengnhat.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

        public final ImageView headerImage;     //We only need to specify components in Card View
        public final TextView title, clause;

        public ClauseViewHolder(View view) {
            super(view);
            headerImage = (ImageView) view.findViewById(R.id.clause_card_header_image);
            title = (TextView) view.findViewById(R.id.clause_card_title);
            clause = (TextView) view.findViewById(R.id.clause_card_content);

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
        ClauseCardContent content = list.get(position);

        viewHolder.headerImage.setImageResource(content.getHeaderImage());
        viewHolder.title.setText(content.getTitle());
        viewHolder.clause.setText(content.getClause());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
