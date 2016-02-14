package app.learning.fantaster.nhatkyhoctiengnhat.database.diary;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

import app.learning.fantaster.nhatkyhoctiengnhat.data.DateOnDiary;
import app.learning.fantaster.nhatkyhoctiengnhat.data.DiaryEntry;

public class DiaryDAO {

    private DiaryDatabaseHelper databaseHelper;

    public DiaryDAO(DiaryDatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /* ====================== CREATE ====================== */

    public void addDiaryEntry(DiaryEntry newDiaryEntry) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DiaryContract.COLUMN_CLAUSE, newDiaryEntry.entryClause);
        contentValues.put(DiaryContract.COLUMN_TOPIC, newDiaryEntry.entryTopic);
        contentValues.put(DiaryContract.COLUMN_RECENT_STUDY, newDiaryEntry.entryRecentStudy);
        contentValues.put(DiaryContract.COLUMN_NEXT_STUDY, newDiaryEntry.entryNextStudy);

        database.insert(DiaryContract.TABLE_NAME, null, contentValues);
    }

    /* ====================== READ ====================== */

    public ArrayList<String> getRecentStudyDates() {
        ArrayList<String> recentStudyDates = new ArrayList<>();
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String commandSQL = "select " + DiaryContract.COLUMN_RECENT_STUDY + " from " + DiaryContract.TABLE_NAME;

        Cursor cursor = database.rawQuery(commandSQL, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                recentStudyDates.add(cursor.getString(0));
            } while (cursor.moveToNext());
            cursor.close();
        }

        LinkedHashSet<String> temp = new LinkedHashSet<>(recentStudyDates);
        recentStudyDates.clear();
        recentStudyDates.addAll(temp);

        Collections.reverse(recentStudyDates);
        return recentStudyDates;
    }

    public ArrayList<DiaryEntry> getEntriesOnDate(String date) {
        ArrayList<DiaryEntry> entries = new ArrayList<>();
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String commandSQL = "select * from " + DiaryContract.TABLE_NAME
                            + " where " + DiaryContract.COLUMN_RECENT_STUDY + "=\'" + date + "\'";

        Cursor cursor = database.rawQuery(commandSQL, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                entries.add(new DiaryEntry(
                        cursor.getInt(cursor.getColumnIndex(DiaryContract.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(DiaryContract.COLUMN_CLAUSE)),
                        cursor.getString(cursor.getColumnIndex(DiaryContract.COLUMN_TOPIC)),
                        cursor.getString(cursor.getColumnIndex(DiaryContract.COLUMN_RECENT_STUDY)),
                        cursor.getString(cursor.getColumnIndex(DiaryContract.COLUMN_NEXT_STUDY))
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return entries;
    }

    public ArrayList<DateOnDiary> getDatesOnDiary() {
        ArrayList<DateOnDiary> datesOnDiary = new ArrayList<>();
        ArrayList<String> allrecentStudyDates = getRecentStudyDates();
        String currentRecentStudyDate = "";
        for (int i = 0; i < allrecentStudyDates.size(); i++) {
            currentRecentStudyDate = allrecentStudyDates.get(i);
            datesOnDiary.add(new DateOnDiary(
                    currentRecentStudyDate,
                    getEntriesOnDate(currentRecentStudyDate)
            ));
        }
        return datesOnDiary;
    }

    /* ====================== UPDATE ====================== */
    /**
     * Let users later specify when they want to study this clause again. This is the only column that we can change
     * @param entryId which entry?
     * @param newEntryNextStudy when users want to study this clause again
     */
    public void updateNextStudy(int entryId, String newEntryNextStudy) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DiaryContract.COLUMN_NEXT_STUDY, newEntryNextStudy);
        database.update(
                DiaryContract.TABLE_NAME,
                contentValues,
                DiaryContract.COLUMN_ID + "=?",
                new String[]{String.valueOf(entryId)}
        );
    }
}
