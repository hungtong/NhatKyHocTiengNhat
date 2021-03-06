package app.learning.fantaster.nhatkyhoctiengnhat.database.clause;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
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

    /* ====================== CREATE ====================== */

    /**
     *  Adding example is pretty simple since we don't have to check whether new example has already existed or not.
     *  In fact, being picky about one subtle change does not worth it.
     *  @param clauseId which clause has new example
     * @param newExample new example added
     */
    public void addExample(int clauseId, String newExample) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ClauseContract.COLUMN_CLAUSE_ID, clauseId);
        contentValues.put(ClauseContract.COLUMN_EXAMPLE, newExample);

        database.insert(ClauseContract.TABLE_EXAMPLES, null, contentValues);
    }

    /**
     *  Adding memory trick is pretty simple since we don't have to check whether new memory trick has already existed or not.
     *  In fact, being picky about one subtle change does not worth it.
     * @param clauseId which clause has new memory trick
     * @param newMemoryTrick new memory trick added
     */
    public void addMemoryTrick(int clauseId, String newMemoryTrick) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ClauseContract.COLUMN_CLAUSE_ID, clauseId);
        contentValues.put(ClauseContract.COLUMN_MEMORY_TRICK, newMemoryTrick);

        database.insert(ClauseContract.TABLE_MEMORY_TRICKS, null, contentValues);
    }

    /* ====================================== READ ====================================== */

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
                            " where " + ClauseContract.COLUMN_CLAUSE_ID + "=" + clauseId;
        Cursor cursor = database.rawQuery(commandSQL, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                examples.add(cursor.getString(cursor.getColumnIndex(ClauseContract.COLUMN_EXAMPLE)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        Collections.reverse(examples); // when we add, new member will be at the end, but when display it should be on top
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
                            " where " + ClauseContract.COLUMN_CLAUSE_ID + "=" + clauseId;
        Cursor cursor = database.rawQuery(commandSQL, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                memoryTricks.add(cursor.getString(cursor.getColumnIndex(ClauseContract.COLUMN_MEMORY_TRICK)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        Collections.reverse(memoryTricks);
        return memoryTricks;
    }

    /**
     * Get all topics by the given clauseId. Since this is many-to-many, the task is more complicated.
     * The ClausesTopics table is the bridge in the relationship and it only containes Id so we need to refer back to
     * Topics Table
     * @param clauseId which clauses
     * @return all topics belonged to the clause whose given clause id
     */
    public ArrayList<String> getTopicsByClause(int clauseId) {
        ArrayList<String> topics = new ArrayList<>();
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String commandSQL = "select * " + "from " +
                            ClauseContract.TABLE_CLAUSES_TOPICS + " as tableClausesTopics," +
                            ClauseContract.TABLE_TOPICS + " as tableTopics" +
                            " where tableClausesTopics." + ClauseContract.COLUMN_CLAUSE_ID + "=" +clauseId + " and" +
                            " tableClausesTopics." + ClauseContract.COLUMN_TOPIC_ID + "=" + "tableTopics." + ClauseContract.COLUMN_ID;
        Cursor cursor = database.rawQuery(commandSQL, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                topics.add(cursor.getString(cursor.getColumnIndex(ClauseContract.COLUMN_TOPIC)));
            } while (cursor.moveToNext());
            cursor.close();
        }
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
            cursor.close();
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
            cursor.close();
        }
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

    /* ============================== UPDATE ============================== */

    public void updateTableClauses(Clause clause) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues updatedValues = new ContentValues();

        updatedValues.put(ClauseContract.COLUMN_ID, clause.clauseId);   // ID should not be changed at all
        updatedValues.put(ClauseContract.COLUMN_CLAUSE_NAME, clause.clause);
        updatedValues.put(ClauseContract.COLUMN_FORMULA, clause.formula);
        updatedValues.put(ClauseContract.COLUMN_BRIEF_SUMMARY, clause.briefSummary);
        updatedValues.put(ClauseContract.COLUMN_EXPLANATION, clause.explanation);
        updatedValues.put(ClauseContract.COLUMN_LAST_EXAMPLE_ON, clause.lastExampleOn);

        String where = ClauseContract.COLUMN_ID + "=?";
        String[] whereArgs = { String.valueOf(clause.clauseId) };

        database.update(ClauseContract.TABLE_CLAUSES, updatedValues, where, whereArgs);
    }

    public void updateTableExamples(String originalExample, String newExample) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues updatedExamples = new ContentValues();

        updatedExamples.put(ClauseContract.COLUMN_EXAMPLE, newExample);
        database.update(ClauseContract.TABLE_EXAMPLES, updatedExamples,
                        ClauseContract.COLUMN_EXAMPLE + "=?", new String[] { originalExample } );
    }

    public void updateTableMemoryTricks(String originalMemoryTrick, String newMemoryTrick) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues updatedMemoryTricks = new ContentValues();

        updatedMemoryTricks.put(ClauseContract.COLUMN_MEMORY_TRICK, newMemoryTrick);
        database.update(ClauseContract.TABLE_MEMORY_TRICKS, updatedMemoryTricks,
                ClauseContract.COLUMN_MEMORY_TRICK + "=?", new String[] { originalMemoryTrick } );
    }

     /* ============================== DELETE ============================== */

    /**
     * Delete the example at a given id
     * @param example which clause's example
     */
    public void deleteExample(String example) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        database.delete(ClauseContract.TABLE_EXAMPLES,
                        ClauseContract.COLUMN_EXAMPLE + "=?",
                        new String[] { example });
    }

    /**
     * Delete the memory trick at a given id
     * @param memoryTrick which clause's memory trick
     */
    public void deleteMemoryTrick(String memoryTrick) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        database.delete(ClauseContract.TABLE_MEMORY_TRICKS,
                ClauseContract.COLUMN_MEMORY_TRICK + "=?",
                new String[] { memoryTrick });
    }
}
