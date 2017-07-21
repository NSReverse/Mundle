package net.nsreverse.mundle.data.notesprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Robert on 7/21/2017.
 */
public class NotesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";
    private static final int VERSION = 1;
    public static final String CREATE_TABLE = "CREATE TABLE " +
            NotesContract.NotesEntry.TABLE_NAME + " (" +
            NotesContract.NotesEntry._ID + " INTEGER PRIMARY KEY, " +
            NotesContract.NotesEntry.COLUMN_AUTHOR_ID + " TEXT NOT NULL, " +
            NotesContract.NotesEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
            NotesContract.NotesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
            NotesContract.NotesEntry.COLUMN_CONTENT + " TEXT NOT NULL);";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " +
            NotesContract.NotesEntry.TABLE_NAME;

    NotesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
