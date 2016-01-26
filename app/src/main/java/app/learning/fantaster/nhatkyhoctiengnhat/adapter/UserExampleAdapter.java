package app.learning.fantaster.nhatkyhoctiengnhat.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.learning.fantaster.nhatkyhoctiengnhat.R;

// Used for memory trick and example
public class UserExampleAdapter extends RecyclerView.Adapter<UserExampleAdapter.AddOnHolder> {

    private Activity context;
    private ArrayList<String> list;
    private static UserExampleListener listener;

    public interface UserExampleListener {
        void onDelete(int position);
        void onModify(int position);
    }

    public UserExampleAdapter(Activity context, ArrayList<String> list, UserExampleListener concreteListener) {
        this.context = context;
        this.list = list;
        this.listener = concreteListener;
    }

    public static class AddOnHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public AddOnHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.add_on_item);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onModify(getAdapterPosition());
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onDelete(getAdapterPosition());
                    return true;
                }
            });
        }
    }

    @Override
    public AddOnHolder onCreateViewHolder(ViewGroup container,int viewType) {
        View view = context.getLayoutInflater().inflate(R.layout.user_add_on, container, false);
        return new AddOnHolder(view);
    }

    @Override
    public void onBindViewHolder(AddOnHolder viewHolder, int position) {
        viewHolder.textView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
