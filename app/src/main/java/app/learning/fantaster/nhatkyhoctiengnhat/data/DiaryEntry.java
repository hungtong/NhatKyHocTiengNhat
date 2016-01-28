package app.learning.fantaster.nhatkyhoctiengnhat.data;

import android.os.Parcel;
import android.os.Parcelable;

public class DiaryEntry implements Parcelable {
    public int entryId;
    public String entryClause;
    public String entryTopic;;
    public String entryRecentStudy;
    public String entryNextStudy;

    public DiaryEntry(int entryId, String entryClause, String entryTopic, String entryRecentStudy, String entryNextStudy) {
        this.entryId = entryId;
        this.entryClause = entryClause;
        this.entryTopic = entryTopic;
        this.entryRecentStudy = entryRecentStudy;
        this.entryNextStudy = entryNextStudy;
    }

    public DiaryEntry(Parcel in) {
        entryId = in.readInt();
        entryClause = in.readString();
        entryTopic = in.readString();
        entryRecentStudy = in.readString();
        entryNextStudy = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(entryId);
        out.writeString(entryClause);
        out.writeString(entryTopic);
        out.writeString(entryRecentStudy);
        out.writeString(entryNextStudy);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<DiaryEntry> CREATOR
            = new Parcelable.Creator<DiaryEntry>() {

        @Override
        public DiaryEntry[] newArray(int size) {
            return new DiaryEntry[size];
        }

        /**
         * With data written in the parcel, new Question(in) read those data and create new Parcelable class
         */
        @Override
        public DiaryEntry createFromParcel(Parcel in) {
            return new DiaryEntry(in);
        }
    };

}
