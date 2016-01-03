package app.learning.fantaster.nhatkyhoctiengnhat.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.learning.fantaster.nhatkyhoctiengnhat.R;

public class TopicSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity context;
    private ArrayList<String> listTopic;
    private TopicSelectedListener listener;

    public interface TopicSelectedListener {
        void onTopicSelectedListener(String contentAtThisPosition);
    }

    public TopicSearchAdapter(Activity context, ArrayList<String> listTopic, TopicSelectedListener listener) {
        this.context = context;
        this.listTopic = listTopic;
        this.listener = listener;
    }

    class TopicHolder extends RecyclerView.ViewHolder {
        public TextView topic;

        public TopicHolder(View view) {
            super(view);
            topic = (TextView) view.findViewById(R.id.topic_item);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTopicSelectedListener(listTopic.get(getAdapterPosition()));
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
        final View view = context.getLayoutInflater().inflate(R.layout.topic_item, container, false);
        return new TopicHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((TopicHolder) viewHolder).topic.setText(listTopic.get(position));
    }

    @Override
    public int getItemCount() {
        return listTopic.size();
    }

}
