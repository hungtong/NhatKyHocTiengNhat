package app.learning.fantaster.nhatkyhoctiengnhat.data;

import android.os.Parcel;
import android.os.Parcelable;

public class DiaryEntry implements Parcelable {
    public int entryId;
    public String entryClause;
    public String entryTopic;;
    public String entryRecentStudy;
    public String entryNextStudy;
    public String writtenOn;

    public DiaryEntry(int entryId, String entryClause, String entryTopic,
                      String entryRecentStudy, String entryNextStudy, String writtenOn) {
        this.entryId = entryId;
        this.entryClause = entryClause;
        this.entryTopic = entryTopic;
        this.entryRecentStudy = entryRecentStudy;
        this.entryNextStudy = entryNextStudy;
        this.writtenOn = writtenOn;
    }

    public DiaryEntry(Parcel in) {
        entryId = in.readInt();
        entryClause = in.readString();
        entryTopic = in.readString();
        entryRecentStudy = in.readString();
        entryNextStudy = in.readString();
        writtenOn = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(entryId);
        out.writeString(entryClause);
        out.writeString(entryTopic);
        out.writeString(entryRecentStudy);
        out.writeString(entryNextStudy);
        out.writeString(writtenOn);
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

        @Override
        public DiaryEntry createFromParcel(Parcel in) {
            return new DiaryEntry(in);
        }
    };

}
