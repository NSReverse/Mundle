package net.nsreverse.mundle.data.notesprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * NotesContentProvider -
 *
 * This class defines the behavior for the Content Provider that manages the Notes that an
 * individual makes while logged in. Meets Udacity's requirements.
 *
 * @author Robert
 * Created on 7/21/2017.
 */
public class NotesContentProvider extends ContentProvider {
    public static final int NOTES = 100;
    public static final int NOTES_WITH_ID = 101;

    private NotesDbHelper notesDbHelper;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    /**
     * buildUriMatcher() -
     *
     * This method creates a UriMatcher for this ContentProvider to use when resolving URIs.
     *
     * @return A UriMatcher to resolve this app's URIs.
     */
    private static UriMatcher buildUriMatcher() {
        UriMatcher newUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        newUriMatcher.addURI(NotesContract.AUTHORITY,
                NotesContract.PATH_NOTES, NOTES);
        newUriMatcher.addURI(NotesContract.AUTHORITY,
                NotesContract.PATH_NOTES + "/#", NOTES_WITH_ID);

        return newUriMatcher;
    }

    /**
     * onCreate() -
     *
     * This method sets up the ContentProvider.
     *
     * @return A boolean representing success. Always true.
     */
    @Override
    public boolean onCreate() {
        Context context = getContext();
        notesDbHelper = new NotesDbHelper(context);
        return true;
    }

    /**
     * insert(Uri, ContentValues) -
     *
     * This method inserts data into the database with the specified values.
     *
     * @param uri The URI indicating the path that the data should be inserted into.
     * @param contentValues A ContentValues object containing key-value data to insert
     *                      into the database.
     * @return A Uri representing the path of the inserted value.
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = notesDbHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case NOTES_WITH_ID:
                long id = db.insert(NotesContract.NotesEntry.TABLE_NAME, null, contentValues);

                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(
                            NotesContract.NotesEntry.CONTENT_URI, id);
                }
                else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (getContext() != null) {
            assert getContext() != null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    /**
     * query(Uri, String[], String, String[], String) -
     *
     * This method retrieves data from the specified URI.
     *
     * @param uri The URI which to retrieve the data.
     * @param projection Unused.
     * @param selection Unused.
     * @param selectionArgs Unused.
     * @param sortOrder Unused.
     * @return A Cursor for viewing data from the database.
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        final SQLiteDatabase db = notesDbHelper.getReadableDatabase();

        int match = uriMatcher.match(uri);
        Cursor returnedCursor;

        switch (match) {
            case NOTES:
                returnedCursor = db.query(NotesContract.NotesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (getContext() != null) {
            assert getContext() != null;
        }

        returnedCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnedCursor;
    }

    /**
     * delete(Uri, String, String[]) -
     *
     * This method deletes data from the specified URI.
     *
     * @param uri The URI in which to delete data.
     * @param s The selection parameter.
     * @param strings The selection arguments parameter.
     * @return An int representing the number of deleted rows.
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = notesDbHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        switch (match) {
            case NOTES:
                db.execSQL(NotesDbHelper.DROP_TABLE);
                db.execSQL(NotesDbHelper.CREATE_TABLE);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (getContext() != null) {
            assert getContext() != null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return 0;
    }

    /**
     * update(Uri, ContentValues, String, String[]) -
     *
     * This method updates a row in the database at the specified URI.
     *
     * @param uri The URI in which to update data.
     * @param contentValues A ContentValues object containing key-value data for updating.
     * @param whereClause A String representing the where clause.
     * @param args A String array containing arguments for the where clause.
     * @return An int representing the count of updated rows.
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String whereClause, @Nullable String[] args) {

        final SQLiteDatabase db = notesDbHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        int result;

        switch (match) {
            case NOTES_WITH_ID:
                result = db.update(NotesContract.NotesEntry.TABLE_NAME,
                        contentValues, whereClause, args);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (getContext() != null) {
            assert getContext() != null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return result;
    }

    /**
     * getType(Uri) -
     *
     * This method gets the mime type for data. Unused. Always returns null.
     *
     * @param uri Unused.
     * @return Null
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
