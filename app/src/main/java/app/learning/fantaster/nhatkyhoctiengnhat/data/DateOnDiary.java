package app.learning.fantaster.nhatkyhoctiengnhat.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;

public class DateOnDiary implements Parcelable, ParentListItem {

    public String date;
    public ArrayList<DiaryEntry> dateEntries;

    public DateOnDiary(String date, ArrayList<DiaryEntry> dateEntries) {
        this.date = date;
        this.dateEntries = dateEntries;
    }

    /**************************      Implement ParentListItem       **************************/
    @Override
    public ArrayList<DiaryEntry> getChildItemList() {
        return dateEntries;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    /**************************      Implement Parcelable       **************************/
    public DateOnDiary(Parcel in) {
        date = in.readString();
        dateEntries = in.readArrayList(String.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(date);
        out.writeTypedList(dateEntries);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<DateOnDiary> CREATOR
            = new Parcelable.Creator<DateOnDiary>() {

        @Override
        public DateOnDiary[] newArray(int size) {
            return new DateOnDiary[size];
        }

        @Override
        public DateOnDiary createFromParcel(Parcel in) {
            return new DateOnDiary(in);
        }

    };

}
