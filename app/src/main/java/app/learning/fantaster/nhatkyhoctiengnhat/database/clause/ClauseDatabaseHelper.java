package app.learning.fantaster.nhatkyhoctiengnhat.database.clause;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

public class ClauseDatabaseHelper extends SQLiteOpenHelper {
    private static ClauseDatabaseHelper instanceClauseDatabaseHelper;

    private final Context context;
    private SQLiteDatabase database;

    // singleton
    public static synchronized ClauseDatabaseHelper getInstance(Context context) {
        if (instanceClauseDatabaseHelper == null) {
            instanceClauseDatabaseHelper = new ClauseDatabaseHelper(context.getApplicationContext());
        }
        return instanceClauseDatabaseHelper;
    }

    public ClauseDatabaseHelper(Context context) {
        super(context, ClauseContract.DATABASE_NAME, null, ClauseContract.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            deleteDatabase();
    }

    /**
     * Check to see whether there already exists a database
     * @return true if there already exists a database, false otherwise
     */
    private boolean checkDatabase() {
        boolean databaseExisted = false;
        try {
            String pathToDatabase = ClauseContract.DATABASE_PATH + ClauseContract.DATABASE_NAME;
            databaseExisted = (new File(pathToDatabase)).exists();
        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.toString());
        }
        return databaseExisted;
    }

    /**
     * If there already exists an database, copy it. The implementation is actually using
     * Stream, copy content from a file and then paste it in another file.
     *
     * Initially, we have a database file in asset file. However, to use that database,
     * one: we directly access to Asset folder by context.getAssets().open => InputStream
     * two: is what this method is all about, copy such InputStream and create an identical
     *      one in data/data/PACKAGE_NAME/databases/ARBITRARY_DATABASE_NAME
     *
     * @throws IOException
     */
    private void copyDatabase() throws IOException {
        String pathToDatabase = ClauseContract.DATABASE_PATH + ClauseContract.DATABASE_NAME;

        InputStream inputStream = context.getAssets().open(ClauseContract.DATABASE_NAME);
        OutputStream outputStream = new FileOutputStream(pathToDatabase);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > -1) {
            outputStream.write(buffer, 0, length);
        }

        inputStream.close();
        outputStream.flush();
        outputStream.close();
    }

    /**
     * Create a new database if there does not yet exist any database.
     */
    public void createDatabase() throws IOException{
        if (!checkDatabase()) { // only create when there is not a database
            this.getReadableDatabase();
            try {
                this.close();
                copyDatabase();
            } catch (IOException ex) {
                Log.d("IOException", ex.toString());
            }
        }
    }

    public void openDatabase() throws SQLException {
        String pathToDatabase = ClauseContract.DATABASE_PATH + ClauseContract.DATABASE_NAME;
        database = SQLiteDatabase.openDatabase(pathToDatabase, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void closeDatabase() {
        if (database != null)
            database.close();
        super.close();
    }


    /**
     *  Delete Database, meaning delete the database file in data/data/PACKAGE_NAME/databases/
     *  not in the asset folder
     */
    public void deleteDatabase() {
        File file = new File(ClauseContract.DATABASE_PATH + ClauseContract.DATABASE_NAME);
        if (file.exists()) {
            database = null;
            file.delete();
        }
    }
}
