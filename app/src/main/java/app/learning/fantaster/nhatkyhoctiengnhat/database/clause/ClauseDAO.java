package app.learning.fantaster.nhatkyhoctiengnhat.database.clause;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import app.learning.fantaster.nhatkyhoctiengnhat.data.Clause;

/**
 * Data Access Object with All CRUD OPERATIONS
 * CRUD : Create, Read, Update and Delete
 */
public class ClauseDAO {

    private ClauseDatabaseHelper databaseHelper;

    public ClauseDAO(ClauseDatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Get all examples belonged to the clause whose given clause id. Pretty much query on Example Table
     * Since it is one-to-many relationship, the task is not so hard
     * @param clauseId given clause id
     * @return all examples belonged to the clause whose given clause id
     */
    public ArrayList<String> getExamplesByClauseId(int clauseId) {
        ArrayList<String> examples = new ArrayList<>();
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String commandSQL = "select "+ ClauseContract.COLUMN_EXAMPLE +
                            " from " + ClauseContract.TABLE_EXAMPLES  +
                            "where " + ClauseContract.COLUMN_CLAUSE_ID + "=" + clauseId;
        Cursor cursor = database.rawQuery(commandSQL, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                examples.add(cursor.getString(cursor.getColumnIndex(ClauseContract.COLUMN_EXAMPLE)));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return examples;
    }

    /**
     * Get all memory tricks belonged to the clause whose given clause id. Pretty much query on MemoryTricks Table
     * Since it is one-to-many relationship, the task is not so hard
     * @param clauseId given clause id
     * @return all memory tricks belonged to the clause whose given clause id
     */
    public ArrayList<String> getMemoryTricksByClauseId(int clauseId) {
        ArrayList<String> memoryTricks = new ArrayList<>();
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String commandSQL = "select " + ClauseContract.COLUMN_MEMORY_TRICK +
                            " from " + ClauseContract.TABLE_MEMORY_TRICKS  +
                            "where " + ClauseContract.COLUMN_CLAUSE_ID + "=" + clauseId;
        Cursor cursor = database.rawQuery(commandSQL, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                memoryTricks.add(cursor.getString(cursor.getColumnIndex(ClauseContract.COLUMN_MEMORY_TRICK)));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return memoryTricks;
    }

    /**
     * Get all topics by the given clauseId. Since this is many-to-many, the task is more complicated.
     * The CLausesTopics table is the bridge in the relationship and it only containes Id so we need to refer back to
     * Topics Table
     * @param clauseId
     * @return
     */
    public ArrayList<String> getTopicsByClause(int clauseId) {
        ArrayList<String> topics = new ArrayList<>();
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String commandSQL = "select * " + "from " +
                            ClauseContract.TABLE_CLAUSES_TOPICS + " as tableClausesTopics," +
                            ClauseContract.TABLE_TOPICS + " as tableTopics" +
                            "where tableClausesTopics." + ClauseContract.COLUMN_CLAUSE_ID + "=" +clauseId + ", " +
                            " tableClausesTopics." + ClauseContract.COLUMN_TOPIC_ID + "=" + "tableTopics." + ClauseContract.COLUMN_ID;
        Cursor cursor = database.rawQuery(commandSQL, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                topics.add(cursor.getString(cursor.getColumnIndex(ClauseContract.COLUMN_TOPIC)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return topics;
    }
    /**
     *  Return the whole table in List<Clause>
     */
    public ArrayList<Clause> getAllClauses()  {
        ArrayList<Clause> list = new ArrayList<>();
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String commandSQL = "select *" + " from " + ClauseContract.TABLE_CLAUSES;

        Cursor cursor = database.rawQuery(commandSQL, null);
        if (cursor != null && cursor.moveToFirst()) {
            Clause currentClause;
            do {
                int _id = cursor.getInt(cursor.getColumnIndex(ClauseContract.COLUMN_ID));
                currentClause = new Clause(
                        _id,                                                                        // ID
                        cursor.getString(cursor.getColumnIndex(ClauseContract.COLUMN_CLAUSE_NAME)), // Clause NAme
                        cursor.getString(cursor.getColumnIndex(ClauseContract.COLUMN_FORMULA)),     // Formula
                        cursor.getString(cursor.getColumnIndex(ClauseContract.COLUMN_BRIEF_SUMMARY)), // Brief Summary
                        cursor.getString(cursor.getColumnIndex(ClauseContract.COLUMN_EXPLANATION)),   // Explanation
                        getExamplesByClauseId(_id),                                                 // Example
                        getTopicsByClause(_id),                                                     // Topics
                        cursor.getString(cursor.getColumnIndex(ClauseContract.COLUMN_LAST_EXAMPLE_ON)), // Last example on
                        getMemoryTricksByClauseId(_id)                                                  // Memory Tricks
                );
                list.add(currentClause);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public ArrayList<String> getTopics() {
        ArrayList<String> topics = new ArrayList<>(getClauseCount());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String commandSQL = "select " + ClauseContract.COLUMN_TOPIC + " from " + ClauseContract.TABLE_TOPICS;
        Cursor cursor = database.rawQuery(commandSQL, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                topics.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();

        // make sure every element only appear one time
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>(topics);
        topics.clear();
        topics.addAll(linkedHashSet);

        return topics;
    }

    public int getClauseCount() {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String commandSQL = "select * from " + ClauseContract.TABLE_CLAUSES;
        Cursor cursor = database.rawQuery(commandSQL, null);
        int count = 0;
        if (cursor != null) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }
    /*
        - Update database when a specific clause changed
        - Specifically, database needs changes on tables:
            +   Clauses (contains PRIMARY KEY)
            +   Examples
            +   Memory Tricks
            +   Clauses-Topics
     */
    public void updateClause(Clause clause) {
        updateTableClauses(clause);
        updateTableExamples(clause);
        updateTableMemoryTricks(clause);
        updateTableClausesTopics(clause);
    }

    public void updateTableClauses(Clause clause) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues updatedValues = new ContentValues();

        updatedValues.put(ClauseContract.COLUMN_ID, clause.clauseId);   // ID should not be changed at all
        updatedValues.put(ClauseContract.COLUMN_CLAUSE_NAME, clause.clause);
        updatedValues.put(ClauseContract.COLUMN_FORMULA, clause.formula);
        updatedValues.put(ClauseContract.COLUMN_BRIEF_SUMMARY, clause.briefSummary);
        updatedValues.put(ClauseContract.COLUMN_EXPLANATION, clause.explanation);

        String where = ClauseContract.COLUMN_ID + "=?";
        String[] whereArgs = { String.valueOf(clause.clauseId) };

        database.update(ClauseContract.TABLE_CLAUSES, updatedValues, where, whereArgs);
    }

    /*
         Apply the strategy clear-and-fill-again.
    */
    public void updateTableExamples(Clause clause) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues updatedValues = new ContentValues();

        // Make IDs in respect to this clause's example null == CLEAR , clause id shoudl remain unchanged
        updatedValues.putNull(ClauseContract.COLUMN_CLAUSE_ID);
        database.update(ClauseContract.TABLE_EXAMPLES, updatedValues, ClauseContract.COLUMN_CLAUSE_ID + "=?",
                new String[]{String.valueOf(clause.clauseId)});

        // FILL AGAIN
        updatedValues.clear();
        int numberOfExamples = clause.example.size();
        if (numberOfExamples != 0)
            for (int i = 0; i < numberOfExamples; i++) {
                updatedValues.put(ClauseContract.COLUMN_CLAUSE_ID, clause.clauseId);// Example must remain the same
                database.update(ClauseContract.TABLE_EXAMPLES, updatedValues,
                        ClauseContract.COLUMN_EXAMPLE + "=?", new String[]{"'" + clause.example.get(i)  + "'"});
            }
    }

    /*
         Apply the strategy clear-and-fill-again since this is one-to-many relationship
    */
    public void updateTableMemoryTricks(Clause clause) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues updatedValues = new ContentValues();

        // Make IDs in respect to this clause's memory trick null == CLEAR
        updatedValues.putNull(ClauseContract.COLUMN_CLAUSE_ID);
        database.update(ClauseContract.TABLE_MEMORY_TRICKS, updatedValues, ClauseContract.COLUMN_ID + "=?",
                new String[] { String.valueOf(clause.clauseId) });

        // FILL AGAIN
        updatedValues.clear();
        int numberOfExamples = clause.example.size();
        if (numberOfExamples != 0)
            for (int i = 0; i < numberOfExamples; i++) {
                updatedValues.put(ClauseContract.COLUMN_CLAUSE_ID, clause.clauseId); // Memory trick must remain the same

                database.update(ClauseContract.TABLE_MEMORY_TRICKS, updatedValues,
                        ClauseContract.COLUMN_MEMORY_TRICK + "=?", new String[]{"'" + clause.example.get(i) + "'"});
            }
    }

    /*
        Apply the strategy of clear-and-fill-again, but in this case we may not fill in everything again (in case of removing), rows
        that were not filled will be discarded. RULE OF THUMBS : Do not touch my little table Topics
     */
    public void updateTableClausesTopics(Clause clause) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues updatedValues = new ContentValues();

        updatedValues.putNull(ClauseContract.COLUMN_TOPIC_ID);
        database.update(ClauseContract.TABLE_CLAUSES_TOPICS, updatedValues,
                        ClauseContract.COLUMN_CLAUSE_ID + "=?", new String[] { String.valueOf(clause.clauseId) });

        int numberOfTopics = clause.topic.size();
        if (numberOfTopics != 0) {
            ArrayList<String> topics = new ArrayList<>(clause.topic);
        }
    }
}
