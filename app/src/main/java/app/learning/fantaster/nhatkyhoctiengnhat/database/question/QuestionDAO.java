package app.learning.fantaster.nhatkyhoctiengnhat.database.question;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;

import app.learning.fantaster.nhatkyhoctiengnhat.data.Question;

/**
 * Data Access Object with All CRUD OPERATIONS
 * CRUD : Create, Read, Update and Delete
 */
public class QuestionDAO {
    private QuestionDatabaseHelper databaseHelper;

    public QuestionDAO(QuestionDatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /* ====================== CREATE ====================== */

    /**
     * Create a row in Table
     */
    public void addQuestion(Question question) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues newQuestion = new ContentValues();

        newQuestion.put(QuestionContract.COLUMN_NAME_QUESTION_ID, question.questionId);
        newQuestion.put(QuestionContract.COLUMN_NAME_QUESTION, question.question);
        newQuestion.put(QuestionContract.COLUMN_NAME_QUESTION_TYPE, question.questionType);
        newQuestion.put(QuestionContract.COLUMN_NAME_CORRECT_ANSWER, question.correctAnswer);
        newQuestion.put(QuestionContract.COLUMN_NAME_DISTRACTOR_1, question.distractor1);
        newQuestion.put(QuestionContract.COLUMN_NAME_DISTRACTOR_2, question.distractor2);
        newQuestion.put(QuestionContract.COLUMN_NAME_DISTRACTOR_3, question.distractor3);
        newQuestion.put(QuestionContract.COLUMN_NAME_EXPLANATION, question.explanation);
        newQuestion.put(QuestionContract.COLUMN_NAME_TRANSLATION, question.translation);

        // in case newQuestion is null, numColumnHack will return a name so that empty row since
        // there must be at least a name to establish a row.
        database.insert(QuestionContract.TABLE_NAME, null, newQuestion);
    }


    /* ====================== READ ====================== */
    /**
     *  Return a row
     *  In this very first time using query, I want to be super detailed about parameters needed.
     *  Setting null sounds like a very smart idea, but it is not encouraged
     */
    public Question getQuestion(int questionId) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String table = QuestionContract.TABLE_NAME;

        // Colunms used to return in this situation, just to be clear at this point, in fact, if return
        // all columns, simply put null
        String[] columns = {
                QuestionContract.COLUMN_NAME_QUESTION_ID, QuestionContract.COLUMN_NAME_QUESTION,
                QuestionContract.COLUMN_NAME_QUESTION_TYPE, QuestionContract.COLUMN_NAME_CORRECT_ANSWER,
                QuestionContract.COLUMN_NAME_DISTRACTOR_1,
                QuestionContract.COLUMN_NAME_DISTRACTOR_2,
                QuestionContract.COLUMN_NAME_DISTRACTOR_3,
                QuestionContract.COLUMN_NAME_EXPLANATION, QuestionContract.COLUMN_NAME_TRANSLATION
        };

        // Point to rows needed to return, full command is WHERE Column_Name = '' or number
        // AND and OR, generally everything that indicates position in SQL, can be applied
        // =? , question mark is generic, representing a value specified later in selectionArgs
        //      respectively
        String selection = QuestionContract.COLUMN_NAME_QUESTION_ID + "=?";

        // Specify for '?'
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
        Question newQuestion = null;
        if (cursor != null) {
            cursor.moveToFirst(); // Move the cursor to the beginning row

            newQuestion = new Question(cursor.getInt(0), //  questionId
                    cursor.getString(1), // question
                    cursor.getInt(2), // questionType
                    cursor.getString(3), // correctAnswer
                    cursor.getString(4), // distractor1
                    cursor.getString(5), // distractor2
                    cursor.getString(6), // distractor3
                    cursor.getString(7), // translation
                    cursor.getString(8)  // explanation
            );
            cursor.close();

        }

        return newQuestion;
    }

    /**
     *  Return the whole table in List<Question>
     */
    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> list = new ArrayList<>();
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String commandSQL = "SELECT * FROM " + QuestionContract.TABLE_NAME;

        // Unlike query(...bunch of stuff...), rawQuery using a command edited.
        Cursor cursor = database.rawQuery(commandSQL, null);
        if (cursor != null && cursor.moveToFirst()) {
            Question newQuestion;
            do {
                newQuestion = new Question(cursor.getInt(0), //  questionId
                        cursor.getString(1), // question
                        cursor.getInt(2), // questionType
                        cursor.getString(3), // correctAnswer
                        cursor.getString(4), // distractor1
                        cursor.getString(5), // distractor2
                        cursor.getString(6), // distractor3
                        cursor.getString(7), // translation
                        cursor.getString(8)  // explanation
                );
                list.add(newQuestion);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return list;
    }

    /**
     * Return a all questions in the table which belong to a specific type
     * @param questionType question type needed
     * @return all questions that belong to question type needed
     */
    public ArrayList<Question> getAllQuestionsByType(int questionType) {
        ArrayList<Question> list = new ArrayList<>();
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String selection = QuestionContract.COLUMN_NAME_QUESTION_TYPE + "=?";
        String[] selectionArgs = {String.valueOf(questionType)};

        Cursor cursor = database.query(QuestionContract.TABLE_NAME, null, selection, selectionArgs,
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Question newQuestion;
            do {
                newQuestion = new Question(cursor.getInt(0), //  questionId
                        cursor.getString(1), // question
                        cursor.getInt(2), // questionType
                        cursor.getString(3), // correctAnswer
                        cursor.getString(4), // distractor1
                        cursor.getString(5), // distractor2
                        cursor.getString(6), // distractor3
                        cursor.getString(7), // translation
                        cursor.getString(8)  // explanation
                );
                list.add(newQuestion);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return list;
    }

    /**
     * Return a number of questions which belong to a specific type in the table. These questions should
     * be shuffled before being returned
     * @param numberOfQuestion how many questions needed to return
     * @param questionType what type of question
     * @return an exact number of questions required or everything left in stock.
     */
    public ArrayList<Question> getRandomTypeQuestions(int numberOfQuestion, int questionType) {
        ArrayList<Question> list = getAllQuestionsByType(questionType);
        Collections.shuffle(list);
        if (list.size() >  numberOfQuestion) {
            for (int i = list.size() - 1; i >= numberOfQuestion; i--)
                list.remove(i);
        }
        return list;
    }

    /**
     *  Return the total number of rows
     */
    public int getQuestionCount() {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String commnadSQL = "select * from" + QuestionContract.TABLE_NAME;
        Cursor cursor = database.rawQuery(commnadSQL, null);
        int count = 0;
        if (cursor != null) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    /* ====================== UPDATE ====================== */

    /**
     *  Make modification on a specific row, the procedure happens like insert.
     *  SQLiteDatabase.update(...) returns the number of rows affected.
     */
    public int updateQuestion(Question question) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        ContentValues updatedQuestion = new ContentValues();

        updatedQuestion.put(QuestionContract.COLUMN_NAME_QUESTION_ID, question.questionId);
        updatedQuestion.put(QuestionContract.COLUMN_NAME_QUESTION, question.question);
        updatedQuestion.put(QuestionContract.COLUMN_NAME_QUESTION_TYPE, question.questionType);
        updatedQuestion.put(QuestionContract.COLUMN_NAME_CORRECT_ANSWER, question.correctAnswer);
        updatedQuestion.put(QuestionContract.COLUMN_NAME_DISTRACTOR_1, question.distractor1);
        updatedQuestion.put(QuestionContract.COLUMN_NAME_DISTRACTOR_2, question.distractor2);
        updatedQuestion.put(QuestionContract.COLUMN_NAME_DISTRACTOR_3, question.distractor3);
        updatedQuestion.put(QuestionContract.COLUMN_NAME_EXPLANATION, question.explanation);
        updatedQuestion.put(QuestionContract.COLUMN_NAME_TRANSLATION, question.translation);

        //Similar to selection and selectionArgs, WHERE ... fill in the ...
        String whereClause = QuestionContract.COLUMN_NAME_QUESTION_ID + "=?";
        String[] whereArgs = { String.valueOf(question.questionId)};

        return database.update(QuestionContract.TABLE_NAME,
                               updatedQuestion,
                               whereClause,
                               whereArgs);
    }

    /* ====================== DELETE ====================== */

    /**
     *  Delete a row with its ID given
     */
    public void deleteQuestion(Question question) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        String whereClause = QuestionContract.COLUMN_NAME_QUESTION_ID + "=?";
        String[] whereArgs = { String.valueOf(question.questionId)};

        database.delete(QuestionContract.TABLE_NAME,
                        whereClause,
                        whereArgs);

    }

}
