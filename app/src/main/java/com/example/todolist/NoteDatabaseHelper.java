package com.example.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDatabaseHelper extends SQLiteOpenHelper  {
    private static final String DB_NAME = "NotesDB";
    private static final int SCH_VERSION = 1;

    public static final String TABLE = "Notes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOTE_TITLE = "note_title";
    public static final String COLUMN_DESCRIPTION = "description";

    public NoteDatabaseHelper (Context context) {
        super(context, DB_NAME, null, SCH_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE Notes"
                        + "("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_NOTE_TITLE + " TEXT, "
                        + COLUMN_DESCRIPTION + " TEXT"
                        + ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
