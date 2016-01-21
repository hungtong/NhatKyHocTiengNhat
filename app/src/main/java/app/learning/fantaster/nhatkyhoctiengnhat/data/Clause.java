package app.learning.fantaster.nhatkyhoctiengnhat.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Clause implements Parcelable {
    public int clauseId;
    public String clause;
    public String formula;
    public String briefSummary;
    public String explanation;
    public ArrayList<String> example;
    public ArrayList<String> topic;
    public String lastExampleOn;
    public ArrayList<String> memoryTrick;


    public Clause(int clauseId, String clause, String formula, String briefSummary,
                    String explanation, ArrayList<String> example, ArrayList<String> topic,
                    String lastExampleOn, ArrayList<String> memoryTrick) {
        this.clauseId = clauseId;
        this.clause = clause;
        this.formula = formula;
        this.briefSummary = briefSummary;
        this.explanation = explanation;
        this.example = example;
        this.topic = topic;
        this.lastExampleOn = lastExampleOn;
        this.memoryTrick = memoryTrick;
    }

    public Clause() {
        example = new ArrayList<>();
        topic = new ArrayList<>();
        memoryTrick = new ArrayList<>();
    }

    public Clause(Parcel in) {
        clauseId = in.readInt();
        clause = in.readString();
        formula = in.readString();
        briefSummary = in.readString();
        explanation = in.readString();
        example = in.readArrayList(String.class.getClassLoader());
        topic = in.readArrayList(String.class.getClassLoader());
        lastExampleOn = in.readString();
        memoryTrick = in.readArrayList(String.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(clauseId);
        out.writeString(clause);
        out.writeString(formula);
        out.writeString(briefSummary);
        out.writeString(explanation);
        out.writeList(example);
        out.writeList(topic);
        out.writeString(lastExampleOn);
        out.writeList(memoryTrick);
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
