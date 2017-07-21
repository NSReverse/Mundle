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
 * Created by Robert on 7/21/2017.
 */
public class NotesContentProvider extends ContentProvider {
    public static final int NOTES = 100;
    public static final int NOTES_WITH_ID = 101;

    private NotesDbHelper notesDbHelper;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher newUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        newUriMatcher.addURI(NotesContract.AUTHORITY,
                NotesContract.PATH_NOTES, NOTES);
        newUriMatcher.addURI(NotesContract.AUTHORITY,
                NotesContract.PATH_NOTES + "/#", NOTES_WITH_ID);

        return newUriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        notesDbHelper = new NotesDbHelper(context);
        return true;
    }

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

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String whereClause, @Nullable String[] args) {

        final SQLiteDatabase db = notesDbHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        int result = 0;

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

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
