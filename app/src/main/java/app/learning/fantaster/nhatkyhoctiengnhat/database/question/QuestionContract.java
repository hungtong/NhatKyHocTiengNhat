package app.learning.fantaster.nhatkyhoctiengnhat.database.question;

public class QuestionContract {

        // information about database
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "JLPT.sqlite";
        public static final String DATABASE_PATH = "/data/data/app.learning.fantaster.nhatkyhoctiengnhat/databases/";

        // information about table
        public static final String TABLE_NAME = "JLPTQuestions";
        public static final String COLUMN_NAME_QUESTION_ID = "_id";
        public static final String COLUMN_NAME_QUESTION = "question";
        public static final String COLUMN_NAME_QUESTION_TYPE ="questionType";
        public static final String COLUMN_NAME_CORRECT_ANSWER = "answer";
        public static final String COLUMN_NAME_DISTRACTOR_1 = "distractor1";
        public static final String COLUMN_NAME_DISTRACTOR_2 = "distractor2";
        public static final String COLUMN_NAME_DISTRACTOR_3 = "distractor3";
        public static final String COLUMN_NAME_TRANSLATION = "translation";
        public static final String COLUMN_NAME_EXPLANATION = "explanation";

}
