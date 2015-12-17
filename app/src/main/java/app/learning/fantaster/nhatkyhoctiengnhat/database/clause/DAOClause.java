package app.learning.fantaster.nhatkyhoctiengnhat.database.clause;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import app.learning.fantaster.nhatkyhoctiengnhat.data.Clause;

/**
 * Data Access Object with All CRUD OPERATIONS
 * CRUD : Create, Read, Update and Delete
 */
public class DAOClause {

    private ClauseDatabaseHelper databaseHelper;

    public DAOClause(ClauseDatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

     /* ====================== CREATE ====================== */

    /**
     * Create a row in Table
     */
    public void addClause(Clause clause) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues newClause = new ContentValues();

        newClause.put(ClauseContract.COLUMN_NAME_CLAUSE_ID, clause.clauseId);
        newClause.put(ClauseContract.COLUMN_NAME_CLAUSE, clause.clause);
        newClause.put(ClauseContract.COLUMN_NAME_CLAUSE_TYPE, clause.clauseType);
        newClause.put(ClauseContract.COLUMN_NAME_TRANSLATION , clause.translation);
        newClause.put(ClauseContract.COLUMN_NAME_EXPLANATION, clause.explanation);
        newClause.put(ClauseContract.COLUMN_NAME_COMMENT, clause.comment);
        newClause.put(ClauseContract.COLUMN_NAME_DATE_CREATED, clause.dateCreated);

        database.insert(ClauseContract.TABLE_NAME, null, newClause);
    }

    /* ====================== READ ====================== */

    /**
     *  Return the whole table in List<Clause>
     */
    public ArrayList<Clause> getAllClauses()  {
        ArrayList<Clause> list = new ArrayList<>();
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String commandSQL = "select * from " + ClauseContract.TABLE_NAME;

        Cursor cursor = database.rawQuery(commandSQL, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Clause clause;
            do {
              clause = new Clause(  cursor.getInt(0),                   // clause id
                                    cursor.getString(1),                // clause
                                    cursor.getInt(2),                   // clause type
                                    cursor.getString(3),                // translation
                                    cursor.getString(4),                // explanation
                                    cursor.getString(5),                // comment
                                    cursor.getString(6)                 // dateCreated
              );
              list.add(clause);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }



}
