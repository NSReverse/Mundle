package net.nsreverse.mundle.data.notesprovider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * NotesContract -
 *
 * This class defines the structure of the ContentProvider to hold notes.
 *
 * @author Robert
 * Created on 7/21/2017.
 */
public class NotesContract {
    public static final String AUTHORITY = "net.nsreverse.mundle";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_NOTES = "notes";

    public static final class NotesEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NOTES).build();

        public static final String TABLE_NAME = "notes";

        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_AUTHOR_ID = "author_id";
    }
}
