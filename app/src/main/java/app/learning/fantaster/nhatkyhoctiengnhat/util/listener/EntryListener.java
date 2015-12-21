package app.learning.fantaster.nhatkyhoctiengnhat.util.listener;

import android.view.View;

public interface EntryListener {
    void onDelete(View childPressed, final int position);
    void onCloserView(View childPressed, final int position);
}
