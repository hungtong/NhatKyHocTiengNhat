package app.learning.fantaster.nhatkyhoctiengnhat.data;

public class Question {
    public int questionId;
    public String question;
    public String questionType;
    public String correctAnswer;
    public String distractor1, distractor2, distractor3;
    public String translation;
    public String explanation;

    public Question(int questionId, String question, String questionType,
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

}
