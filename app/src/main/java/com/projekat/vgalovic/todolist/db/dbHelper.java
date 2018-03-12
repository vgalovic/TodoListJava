package com.projekat.vgalovic.todolist.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vgalovic on 11.02.2018..
 */

public class dbHelper extends SQLiteOpenHelper {

    public dbHelper(Context context){super(context, dbProperties.DB_NAME, null, dbProperties.DB_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + dbProperties.TaskEntry.TABLE + " ( " +
                dbProperties.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                dbProperties.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + dbProperties.TaskEntry.TABLE);
        onCreate(db);
    }
}
