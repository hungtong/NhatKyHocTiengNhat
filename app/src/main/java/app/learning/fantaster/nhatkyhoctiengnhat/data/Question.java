package app.learning.fantaster.nhatkyhoctiengnhat.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {
    public int questionId;
    public String question;
    public int questionType;
    public String correctAnswer;
    public String distractor1, distractor2, distractor3;
    public String translation;
    public String explanation;

    public Question(int questionId, String question, int questionType,
                    String correctAnswer, String distractor1, String distractor2, String distractor3,
                    String translation, String explanation) {

        this.questionId = questionId;
        this.question = question;
        this.questionType = questionType;
        this.correctAnswer = correctAnswer;
        this.distractor1 = distractor1;
        this.distractor2 = distractor2;
        this.distractor3 = distractor3;
        this.translation = translation;
        this.explanation = explanation;

    }

    public Question(Parcel in) {
        questionId = in.readInt();
        question = in.readString();
        questionType = in.readInt();
        correctAnswer = in.readString();
        distractor1 = in.readString();
        distractor2 = in.readString();
        distractor3 = in.readString();
        translation = in.readString();
        explanation = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(questionId);
        out.writeString(question);
        out.writeInt(questionType);
        out.writeString(correctAnswer);
        out.writeString(distractor1);
        out.writeString(distractor2);
        out.writeString(distractor3);
        out.writeString(translation);
        out.writeString(explanation);
    }

    public static final Parcelable.Creator<Question> CREATOR
            = new Parcelable.Creator<Question>() {

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }

        /**
         * With data written in the parcel, new Question(in) read those data and create new Parcelable class
         */
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }
    };


}
