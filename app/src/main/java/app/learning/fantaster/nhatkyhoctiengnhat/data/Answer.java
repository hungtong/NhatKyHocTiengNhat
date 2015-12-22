package app.learning.fantaster.nhatkyhoctiengnhat.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Answer implements Parcelable {

    public String question;
    public String answer;
    public String correctAnswer;
    public int correctOrNot;
    public int attemptedOrNot;

    public Answer(String question, String answer, String correctAnswer, int correctOrNot, int attemptedOrNot) {
        this.question = question;
        this.answer = answer;
        this.correctAnswer = correctAnswer;
        this.correctOrNot = correctOrNot;
        this.attemptedOrNot = attemptedOrNot;
    }

    public Answer(String question, String correctAnswer) {
        this(question, null, correctAnswer, 0, 0);
    }

    public Answer(Parcel in) {
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

    public static final Parcelable.Creator<Answer> CREATOR =
            new Parcelable.Creator<Answer>() {
                @Override
                public Answer[] newArray(int size) {
                    return new Answer[size];
                }

                /**
                 * With data written in the parcel, new Question(in) read those data and create new Parcelable class
                 */
                @Override
                public Answer createFromParcel(Parcel in) {
                    return new Answer(in);
                }
            };



}
