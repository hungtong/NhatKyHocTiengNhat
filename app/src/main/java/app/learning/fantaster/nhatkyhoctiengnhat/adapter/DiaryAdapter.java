package app.learning.fantaster.nhatkyhoctiengnhat.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import app.learning.fantaster.nhatkyhoctiengnhat.R;
import app.learning.fantaster.nhatkyhoctiengnhat.data.DateOnDiary;
import app.learning.fantaster.nhatkyhoctiengnhat.data.DiaryEntry;

public class DiaryAdapter extends ExpandableRecyclerAdapter<DiaryAdapter.DiaryDateHolder, DiaryAdapter.EntryHolder> {

    private Activity context;
    private static ArrayList<DateOnDiary> parents;

    public DiaryAdapter(Activity context, ArrayList<DateOnDiary> concreteParents) {
        super(parents);
        this.context = context;
        parents = concreteParents;
    }

    public static class DiaryDateHolder extends ParentViewHolder {
        public TextView diaryDateIndicator;

        public DiaryDateHolder(View view) {
            super(view);
            diaryDateIndicator = (TextView) view.findViewById(R.id.diary_date_indicator);
        }
    }

    public static class EntryHolder extends ChildViewHolder {
        public TextView entryClause, entryTopic, entryRecentStudy, entryNextStudy;

        public EntryHolder(View view) {
            super(view);
            entryClause = (TextView) view.findViewById(R.id.entry_clause);
            entryTopic = (TextView) view.findViewById(R.id.entry_topic);
            entryRecentStudy = (TextView) view.findViewById(R.id.entry_recent_study);
            entryNextStudy = (TextView) view.findViewById(R.id.entry_next_study);

        }
    }

    @Override
    public DiaryDateHolder onCreateParentViewHolder(ViewGroup container) {
        View view = context.getLayoutInflater().inflate(R.layout.diary_date, container, false);
        return new DiaryDateHolder(view);
    }

    @Override
    public void onBindParentViewHolder(DiaryDateHolder viewHolder, int position, ParentListItem parentListItem) {
        DateOnDiary dateOnDiary = (DateOnDiary) parentListItem;
        String dateToFillIn = dateOnDiary.date;
        if (position < 3) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy");
            Calendar calendar = Calendar.getInstance();
            String today = simpleDateFormat.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            String yesterday = simpleDateFormat.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            String dayBefore = simpleDateFormat.format(calendar.getTime());
            if (dateToFillIn.equals(today))
                dateToFillIn = context.getString(R.string.today);
            if (dateToFillIn.equals(yesterday))
                dateToFillIn = context.getString(R.string.yesterday);
            if (dateToFillIn.equals(dayBefore))
                dateToFillIn = context.getString(R.string.the_day_before);
        }

        viewHolder.diaryDateIndicator.setText(dateToFillIn);
    }

    @Override
    public EntryHolder onCreateChildViewHolder(ViewGroup container) {
        View view = context.getLayoutInflater().inflate(R.layout.diary_entry, container, false);
        return new EntryHolder(view);
    }

    @Override
    public void onBindChildViewHolder(EntryHolder viewHolder, int position, Object childObject) {
        DiaryEntry diaryEntry = (DiaryEntry) childObject;
        viewHolder.entryClause.setText(diaryEntry.entryClause);
        viewHolder.entryTopic.setText(diaryEntry.entryTopic);
        viewHolder.entryRecentStudy.setText(diaryEntry.entryRecentStudy);
        viewHolder.entryNextStudy.setText(diaryEntry.entryNextStudy);
    }
}
