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
                                    cursor.getString(2),                // formula
                                    cursor.getString(3),                // briefSummary
                                    cursor.getString(4),                // explanation
                                    cursor.getString(5),                // example
                                    cursor.getString(6),                 // topic
                                    cursor.getString(7)                 // lastExampleOn
              );
              list.add(clause);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public void updateClause(Clause clause) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        ContentValues updatedClause = new ContentValues();

        updatedClause.put(ClauseContract.COLUMN_NAME_CLAUSE_ID, clause.clauseId);
        updatedClause.put(ClauseContract.COLUMN_NAME_CLAUSE, clause.clause);
        updatedClause.put(ClauseContract.COLUMN_NAME_FORMULA, clause.formula);
        updatedClause.put(ClauseContract.COLUMN_NAME_BRIEF_SUMMARY, clause.briefSummary);
        updatedClause.put(ClauseContract.COLUMN_NAME_EXPLANATION, clause.explanation);
        updatedClause.put(ClauseContract.COLUMN_NAME_EXAMPLE, clause.example);
        updatedClause.put(ClauseContract.COLUMN_NAME_TOPIC, clause.topic);
        updatedClause.put(ClauseContract.COLUNM_NAME_LAST_EXAMPLE_ON, clause.lastExampleOn);

        String whereClause = ClauseContract.COLUMN_NAME_CLAUSE_ID + "=?";
        String[] whereArgs = { String.valueOf(clause.clauseId) };

        database.update(ClauseContract.TABLE_NAME, updatedClause, whereClause, whereArgs);
    }


}
