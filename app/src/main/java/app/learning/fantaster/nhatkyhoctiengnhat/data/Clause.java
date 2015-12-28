package app.learning.fantaster.nhatkyhoctiengnhat.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Clause implements Parcelable {
    public int clauseId;
    public String clause;
    public String formula;
    public String briefSummary;
    public String explanation;
    public String example;
    public String topic;
    public String lastExampleOn;


    public Clause(int clauseId, String clause, String formula, String briefSummary,
                    String explanation, String example, String topic, String lastExampleOn) {
        this.clauseId = clauseId;
        this.clause = clause;
        this.formula = formula;
        this.briefSummary = briefSummary;
        this.explanation = explanation;
        this.example = example;
        this.topic = topic;
        this.lastExampleOn = lastExampleOn;
    }

    public Clause() {}

    public Clause(Parcel in) {
        clauseId = in.readInt();
        clause = in.readString();
        formula = in.readString();
        briefSummary = in.readString();
        explanation = in.readString();
        example = in.readString();
        topic = in.readString();
        lastExampleOn = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(clauseId);
        out.writeString(clause);
        out.writeString(formula);
        out.writeString(briefSummary);
        out.writeString(explanation);
        out.writeString(example);
        out.writeString(topic);
        out.writeString(lastExampleOn);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Clause> CREATOR
            = new Parcelable.Creator<Clause>() {

        @Override
        public Clause[] newArray(int size) {
            return new Clause[size];
        }

        /**
         * With data written in the parcel, new Question(in) read those data and create new Parcelable class
         */
        @Override
        public Clause createFromParcel(Parcel in) {
            return new Clause(in);
        }
    };

}
