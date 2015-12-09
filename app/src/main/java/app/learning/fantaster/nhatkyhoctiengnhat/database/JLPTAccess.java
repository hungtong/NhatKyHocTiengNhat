package app.learning.fantaster.nhatkyhoctiengnhat.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import app.learning.fantaster.nhatkyhoctiengnhat.data.Question;

/**
 * Data Access Object with All CRUD OPERATIONS
 * CRUD : Create, Read, Update and Delete
 */
public class JLPTAccess {
    private SQLiteDatabase database;

    public JLPTAccess(SQLiteDatabase database) {
        this.database = database;
    }

    /* ====================== CREATE ====================== */

    /**
     * Create a row in Table
     */
    public void addQuestion(Question question) {
        ContentValues newQuestion = new ContentValues();

        newQuestion.put(JLPTContract.COLUMN_NAME_QUESTION_ID, question.questionId);
        newQuestion.put(JLPTContract.COLUMN_NAME_QUESTION, question.question);
        newQuestion.put(JLPTContract.COLUMN_NAME_QUESTION_TYPE, question.questionType);
        newQuestion.put(JLPTContract.COLUMN_NAME_CORRECT_ANSWER, question.correctAnswer);
        newQuestion.put(JLPTContract.COLUMN_NAME_DISTRACTOR_1, question.distractor1);
        newQuestion.put(JLPTContract.COLUMN_NAME_DISTRACTOR_2, question.distractor2);
        newQuestion.put(JLPTContract.COLUMN_NAME_DISTRACTOR_3, question.distractor3);
        newQuestion.put(JLPTContract.COLUMN_NAME_EXPLANATION, question.explanation);
        newQuestion.put(JLPTContract.COLUMN_NAME_TRANSLATION, question.translation);

        database.insert(JLPTContract.TABLE_NAME, null, newQuestion);

    }

    /* ====================== READ ====================== */

    /**
     *  In this very first time using query, I want to be super detailed about parameters needed.
     *  Setting sounds like a very smart idea, but it is not encouraged
     */
    public Question getQuestion(int questionId) {
        String table = JLPTContract.TABLE_NAME;

        // Colunms used to return in this situation
        String[] columns = {
                JLPTContract.COLUMN_NAME_QUESTION_ID, JLPTContract.COLUMN_NAME_QUESTION,
                JLPTContract.COLUMN_NAME_QUESTION_TYPE, JLPTContract.COLUMN_NAME_CORRECT_ANSWER,
                JLPTContract.COLUMN_NAME_DISTRACTOR_1,
                JLPTContract.COLUMN_NAME_DISTRACTOR_2,
                JLPTContract.COLUMN_NAME_DISTRACTOR_3,
                JLPTContract.COLUMN_NAME_EXPLANATION, JLPTContract.COLUMN_NAME_TRANSLATION
        };

        // Point to rows needed to return, full command is WHERE Column_Name = '' or number
        // AND and OR, generally everything that indicates position in SQL, can be applied
        // =? , question mark is generic, representing a value specified later in selectionArgs
        //      respectively
        String selection = JLPTContract.COLUMN_NAME_QUESTION_ID + "=?";

        // Specify for '?'
        // I don't think that in the context of Android Programming, ? can be numberic
        //      so it is necessary to convert a integer into String
        String[] selectionArgs = { String.valueOf(questionId) };

        // GROUP BY ... , fill in ... , null not to be grouped
        String groupBy = null;

        // HAVING ... , fill in ... => learn SQL
        String having = null;

        // ORDER BY ... , fill in ... => since there is no special reason to do so, set null
        String orderBy = null;

        // LIMIT ..., fill in ... => at most how many rows to return
        String limit = null;

        // In Vietnamese, Cursor is "Con trỏ," unfortunately its use is not life Mouse Cursor
        // In fact, we have to use commands to move it. In other words, it is quite manual
        Cursor cursor = database.query(table, columns, selection, selectionArgs,
                                        groupBy, having, orderBy, limit);
        if (cursor != null)
            cursor.moveToFirst(); // Move the cursor to the beginning row

        return new Question(Integer.parseInt(cursor.getString(0)), //  questionId
                            cursor.getString(1), // question
                            cursor.getString(2), // questionType
                            cursor.getString(3), // correctAnswer
                            cursor.getString(4), // distractor1
                            cursor.getString(5), // distractor2
                            cursor.getString(6), // distractor3
                            cursor.getString(7), // translation
                            cursor.getString(8)  // explanation
        );
    }
}
