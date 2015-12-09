package app.learning.fantaster.nhatkyhoctiengnhat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

public class JLPTQuestionDatabaseHelper extends SQLiteOpenHelper {

    private final Context context;
    private SQLiteDatabase database;

    public JLPTQuestionDatabaseHelper(Context context) {
        super(context, JLPTContract.DATABASE_NAME, null, JLPTContract.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Check to see whether there already exists a database
     * @return true if there already exists a database, false otherwise
     */
    private boolean checkDatabase() {
        boolean databaseExisted = false;
        try {
            String pathToData = JLPTContract.DATABASE_PATH + JLPTContract.DATABASE_NAME;
            File file  = new File(pathToData);
            databaseExisted = file.exists();
        } catch (SQLiteException ex) {

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
    private void copyDatabase() throws IOException, IndexOutOfBoundsException {
        String pathToDatabase = JLPTContract.DATABASE_PATH + JLPTContract.DATABASE_NAME;

        InputStream inputStream = context.getAssets().open(JLPTContract.DATABASE_NAME);
        OutputStream outputStream = new FileOutputStream(pathToDatabase);


        // This snippet is particularly for copying
        byte[] buffer = new byte[1024]; // YEs, Stream is a source of bytes

        int length; // this variable will indicate when we reach the end of the file
        while ((length = inputStream.read(buffer)) > -1) {
            outputStream.write(buffer, 0, length);  // buffer and length are updated after .read(...)
        }

        /**
         * inputStream.read(byte[] buffer) is equivalent to
         * .read(byte[] buffer, int byteOffSet, int byteCount) with byteOffSet = 0,
         * byteCount = buffer.length
         * When reaching the end of the file, it will return -1, buffer will be updated due to
         * function's implementation, since buffer is static pointer so ...
         */

        inputStream.close();
        outputStream.flush(); // ensure that any buffered data are written out
        outputStream.close();

    }

    /**
     * Create a new database if there does not yet exist any database.
     */
    public void createDatabase() throws IOException, Error {
        boolean databaseExisted = checkDatabase();

        if (databaseExisted) {

        }
        else {
            getReadableDatabase();

            try {
                copyDatabase(); // not existed, copy the one in asset folder to create here
            } catch (IOException ex) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     *  Delete Database, meaning delete the database file in data/data/PACKAGE_NAME/databases/
     *  not in the asset folder
     */
    public void deleteDatabase() {
        File file = new File(JLPTContract.DATABASE_PATH + JLPTContract.DATABASE_NAME);
        if (file.exists()) {
            database = null;
            file.delete();
        }
    }

    public void openDatabase() throws SQLException{
        String pathToDatabase = JLPTContract.DATABASE_PATH + JLPTContract.DATABASE_NAME;
        database = SQLiteDatabase.openDatabase(pathToDatabase, null, SQLiteDatabase.OPEN_READONLY);
    }

    public synchronized void closeDatabse() {
        if (database != null)
            database.close();
        super.close();
    }

}
