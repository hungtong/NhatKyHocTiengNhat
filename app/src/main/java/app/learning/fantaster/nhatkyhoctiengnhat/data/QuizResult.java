package app.learning.fantaster.nhatkyhoctiengnhat.data;

public class QuizResult {
    public String question;
    public String userChoice;
    public String correctAnswer;
    public String translation;
    public String explanation;

    public QuizResult(String question,
                      String userChoice, String correctAnswer,
                      String translation, String explanation) {
        this.question = question;
        this.userChoice = userChoice;
        this.correctAnswer = correctAnswer;
        this.translation = translation;
        this.explanation = explanation;
    }
}
