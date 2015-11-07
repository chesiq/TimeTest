package com.antonk.urantestapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton on 07-Nov-15.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "time_results";
    private static int DB_VERSION = 1;

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIME = "time";

    private static final String TABLE_TIMES = "times";

    private static DatabaseOpenHelper sInstance;

    public static synchronized DatabaseOpenHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new DatabaseOpenHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    private DatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql =     "CREATE TABLE "+ TABLE_TIMES + " ( "
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + COLUMN_TIME + " TEXT NOT NULL )";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //no impl
    }

    public List<String> getData(){
        List<String> data = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_TIMES, null);
        if (c.moveToFirst()){
            do{
                data.add(c.getString(c.getColumnIndex(COLUMN_TIME)));
            }while(c.moveToNext());
        }
        c.close();
        return data;
    }

    public void add(String item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIME, item);
        try {
            db.insert(TABLE_TIMES, null, values);
        }catch (SQLiteException e){
            e.printStackTrace();
        }
    }
}
