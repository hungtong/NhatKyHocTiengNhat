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

public class ClauseAdapter extends RecyclerView.Adapter<ClauseAdapter.ClauseViewHolder> {

    private Activity context;
    private ArrayList<ClauseCardContent> list;

    public ClauseAdapter(Activity context, ArrayList<ClauseCardContent> list) {
        this.context = context;
        this.list = list;
    }

    public static class ClauseViewHolder extends RecyclerView.ViewHolder {
        public final ImageView headerImage;     //We only need to specify components in Card View
        public final TextView title, clause;

        public ClauseViewHolder(View view) {
            super(view);
            headerImage = (ImageView) view.findViewById(R.id.clause_card_header_image);
            title = (TextView) view.findViewById(R.id.clause_card_title);
            clause = (TextView) view.findViewById(R.id.clause_card_content);
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
