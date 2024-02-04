package com.example.myfoodchoice.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "exhibition.db";

    public static final String TABLE_NAME = "exhibition";

    public static final String COL_1 = "ID";

    public static final String COL_2 = "title";

    public static final String COL_3 = "logoID";

    public static final String COL_4 = "dayOfWeek";

    public static final String COL_5 = "startTime";

    public static final String COL_6 = "endTime";

    public static final String COL_7 = "numVisitors";

    public static final String COL_8 = "price";

    public static final String COL_9 = "totalCharges";


    public DbHelper(@Nullable Context context,
                    @Nullable String name,
                    @Nullable SQLiteDatabase.CursorFactory factory,
                    int version)
    {
        super(context, name, factory, version);
    }

    //retrieving all records
    public Cursor viewAllRecords()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    //updating the record matching the id
    public boolean updateRecord(String id, String title,
                                int logoID, String dayOfWeek,
                                String startTime, String endTime,
                                int numVisitor, double price, double totalCharges)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, title);
        contentValues.put(COL_3, logoID);
        contentValues.put(COL_4, dayOfWeek);
        contentValues.put(COL_5, startTime.toString()); // Convert Time to String
        contentValues.put(COL_6, endTime.toString()); // Convert Time to String
        contentValues.put(COL_7, numVisitor);
        contentValues.put(COL_8, price);
        contentValues.put(COL_9, totalCharges);
        db.update(TABLE_NAME, contentValues,"ID = ?",
                new String[] {id});
        return true;
    }

    //adding a record
    public boolean insertData(String title,
                              int logoID, String dayOfWeek,
                              String startTime, String endTime,
                              int numVisitor, double price, double totalCharges)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues(); //This class is used to store a
        //set of values that the ContentResolver can process
        contentValues.put(COL_2, title);
        contentValues.put(COL_3, logoID);
        contentValues.put(COL_4, dayOfWeek);
        contentValues.put(COL_5, startTime.toString());
        contentValues.put(COL_6, endTime.toString());
        contentValues.put(COL_7, numVisitor);
        contentValues.put(COL_8, price);
        contentValues.put(COL_9, totalCharges);

        long result = db.insert(TABLE_NAME, null, contentValues);
        // null - the framework does not insert a row when there are no values
        return result != -1;
    }

    //retrieving single record matching the id
    // Retrieving a single record matching the id
    public Cursor viewRecord(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = new String[]{COL_1, COL_2, COL_3, COL_4, COL_5, COL_6, COL_7, COL_8, COL_9};
        String selection = COL_1 + "=?";
        String[] selectionArgs = new String[]{String.valueOf(id)};

        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        return cursor;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // FIXME: improve version using var to init table.
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2 + " TEXT, " +
                COL_3 + " INTEGER, " +
                COL_4 + " TEXT, " +
                COL_5 + " TEXT, " +
                COL_6 + " TEXT, " +
                COL_7 + " INTEGER, " +
                COL_8 + " REAL, " +
                COL_9 + " REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

