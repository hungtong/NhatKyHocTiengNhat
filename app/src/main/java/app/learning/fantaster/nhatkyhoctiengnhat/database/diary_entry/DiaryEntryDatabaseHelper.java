package app.learning.fantaster.nhatkyhoctiengnhat.database.diary_entry;

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


public class DiaryEntryDatabaseHelper extends SQLiteOpenHelper {

    private static DiaryEntryDatabaseHelper databaseHelper;

    private final Context context;
    private SQLiteDatabase database;

    public DiaryEntryDatabaseHelper(Context context, SQLiteDatabase database) {
        super(context, DiaryEntryContract.DATABASE_NAME, null, DiaryEntryContract.DATABASE_VERSION);
        this.context = context;
        this.database = database;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            deleteDatabase();
    }

    private boolean checkDatabase() {
        boolean isDatabaseExisted = false;
        try {
            String pathToDatabase = DiaryEntryContract.DATABASE_PATH + DiaryEntryContract.DATABASE_NAME;
            isDatabaseExisted = (new File(pathToDatabase)).exists();
        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.toString());
        }
        return  isDatabaseExisted;
    }

    private void copyDatabase() throws IOException{
        String pathToDatabase = DiaryEntryContract.DATABASE_PATH + DiaryEntryContract.DATABASE_NAME;

        InputStream inputStream = context.getAssets().open(DiaryEntryContract.DATABASE_NAME);
        OutputStream outputStream = new FileOutputStream(pathToDatabase);

        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) > -1) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.flush();
        outputStream.close();
    }

    public void createDatabase() {
        if (!checkDatabase()) {
            try {
                copyDatabase();
            } catch (IOException ex) {
                Log.d("IOException", ex.toString());
            }
        }
    }

    public void openDatabase() {
        String pathToDatabase = DiaryEntryContract.DATABASE_PATH + DiaryEntryContract.DATABASE_NAME;
        database = SQLiteDatabase.openDatabase(pathToDatabase, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void closeDatabase() {
        if (database != null) {
            database.close();
        }
        super.close();
    }

    public void deleteDatabase() {
        File file = new File (DiaryEntryContract.DATABASE_PATH + DiaryEntryContract.DATABASE_NAME);
        if (file.exists()) {
            database = null;
            file.delete();
        }
    }

}
