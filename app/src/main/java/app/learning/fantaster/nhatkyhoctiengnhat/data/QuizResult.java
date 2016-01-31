package app.learning.fantaster.nhatkyhoctiengnhat.data;

import android.os.Parcel;
import android.os.Parcelable;

public class QuizResult implements Parcelable {

    public String question;
    public String answer;
    public String correctAnswer;
    public int correctOrNot;
    public int attemptedOrNot;

    public QuizResult(String question, String answer, String correctAnswer, int correctOrNot, int attemptedOrNot) {
        this.question = question;
        this.answer = answer;
        this.correctAnswer = correctAnswer;
        this.correctOrNot = correctOrNot;
        this.attemptedOrNot = attemptedOrNot;
    }

    public QuizResult(String question, String correctAnswer) {
        this(question, null, correctAnswer, 0, 0);
    }

    public QuizResult(Parcel in) {
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

    public static final Parcelable.Creator<QuizResult> CREATOR =
            new Parcelable.Creator<QuizResult>() {
                @Override
                public QuizResult[] newArray(int size) {
                    return new QuizResult[size];
                }

                @Override
                public QuizResult createFromParcel(Parcel in) {
                    return new QuizResult(in);
                }
            };



}
