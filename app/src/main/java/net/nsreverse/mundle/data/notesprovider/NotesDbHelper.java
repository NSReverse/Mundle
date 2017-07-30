package net.nsreverse.mundle.data.notesprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * NotesDbHelper -
 *
 * This class handles the creation and maintenance of the Notes table in the ContentProvider.
 *
 * @author Robert
 * Created on 7/21/2017.
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

    /**
     * Constructor NotesDbHelper(Context) -
     *
     * This is the default constructor for creating a NotesDbHelper
     *
     * @param context The Context to pass to the SQLiteOpenHelper class.
     */
    NotesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * onCreate(SQLiteDatabase) -
     *
     * This method creates the Notes table if it doesn't exist.
     *
     * @param db The SQLiteDatabase to perform operations on.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    /**
     * onUpgrade(SQLiteDatabase, int, int) -
     *
     * This method upgrades the database. Must reimplement.
     *
     * @param db The SQLiteDatabase to perform operations on.
     * @param oldVer An int representing the old version.
     * @param newVer An int representing the new version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
