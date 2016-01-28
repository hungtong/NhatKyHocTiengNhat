package app.learning.fantaster.nhatkyhoctiengnhat.database.diary_entry;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import app.learning.fantaster.nhatkyhoctiengnhat.data.DiaryEntry;

public class DiaryEntryDAO {

    private DiaryEntryDatabaseHelper databaseHelper;

    public DiaryEntryDAO(DiaryEntryDatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void addDiaryEntry(DiaryEntry newDiaryEntry) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DiaryEntryContract.COLUMN_CLAUSE, newDiaryEntry.entryClause);
        contentValues.put(DiaryEntryContract.COLUMN_TOPIC, newDiaryEntry.entryTopic);
        contentValues.put(DiaryEntryContract.COLUMN_RECENT_STUDY, newDiaryEntry.entryRecentStudy);
        contentValues.put(DiaryEntryContract.COLUMN_NEXT_STUDY, newDiaryEntry.entryNextStudy);

        database.insert(DiaryEntryContract.TABLE_NAME, null, contentValues);
    }

    /**
     * Let users later specify when they want to study this clause again. This is the only column that we can change
     * @param entryId which entry?
     * @param newEntryNextStudy when users want to study this clause again
     */
    public void updateNextStudy(int entryId, String newEntryNextStudy) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DiaryEntryContract.COLUMN_NEXT_STUDY, newEntryNextStudy);
        database.update(
                DiaryEntryContract.TABLE_NAME,
                contentValues,
                DiaryEntryContract.COLUMN_ID + "=?",
                new String[]{String.valueOf(entryId)}
        );
    }

    public void deleteDiaryEntry(int entryId) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        database.delete(
                DiaryEntryContract.TABLE_NAME,
                DiaryEntryContract.COLUMN_ID + "=?",
                new String[] { String.valueOf(entryId) }
        );
    }
}
