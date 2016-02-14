package app.learning.fantaster.nhatkyhoctiengnhat.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ExamResult implements Parcelable {

    public String question;
    public String answer;
    public String correctAnswer;
    public int correctOrNot;
    public int attemptedOrNot;

    public ExamResult(String question, String answer, String correctAnswer, int correctOrNot, int attemptedOrNot) {
        this.question = question;
        this.answer = answer;
        this.correctAnswer = correctAnswer;
        this.correctOrNot = correctOrNot;
        this.attemptedOrNot = attemptedOrNot;
    }

    public ExamResult(String question, String correctAnswer) {
        this(question, null, correctAnswer, 0, 0);
    }

    public ExamResult(Parcel in) {
        question = in.readString();
        answer = in.readString();
        correctAnswer = in.readString();
        correctOrNot = in.readInt();
        attemptedOrNot = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(question);
        out.writeString(answer);
        out.writeString(correctAnswer);
        out.writeInt(correctOrNot);
        out.writeInt(attemptedOrNot);
    }

    public static final Parcelable.Creator<ExamResult> CREATOR =
            new Parcelable.Creator<ExamResult>() {
                @Override
                public ExamResult[] newArray(int size) {
                    return new ExamResult[size];
                }

                @Override
                public ExamResult createFromParcel(Parcel in) {
                    return new ExamResult(in);
                }
            };



}
