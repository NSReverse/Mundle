package net.nsreverse.mundle.ui.notes;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import net.nsreverse.mundle.MundleApplication;
import net.nsreverse.mundle.R;
import net.nsreverse.mundle.data.UserDefaults;
import net.nsreverse.mundle.data.notesprovider.NotesContract;
import net.nsreverse.mundle.model.Note;
import net.nsreverse.mundle.ui.adapters.NotesAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ViewNotesActivity extends AppCompatActivity
                               implements NotesAdapter.Delegate,
                                          LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ADD_NOTE_REQUEST = 101;
    private static final int EDIT_NOTE_REQUEST = 102;
    private static final int TASK_LOADER_ID = 201;

    @BindView(R.id.recycler_view_notes_list) RecyclerView notesRecyclerView;
    @BindView(R.id.fab_create_note) FloatingActionButton fabAddNote;

    private Context context;
    private AsyncTask currentTask;
    private NotesAdapter adapter;
    private int contentResolverLastIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getString(R.string.activity_my_notes));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;

        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());

        setupComponents();
    }

    private void setupComponents() {
        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CreateNoteActivity.class);
                intent.putExtra(CreateNoteActivity.KEY_NEXT_INDEX, contentResolverLastIndex + 1);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        setDataSource();
    }

    public void setDataSource() {
        ParseQuery<ParseObject> query = new ParseQuery<>("PersonalNote");
        query.whereEqualTo("author_id", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    getContentResolver().delete(NotesContract.NotesEntry.CONTENT_URI, null, null);
                    getContentResolver().notifyChange(NotesContract.NotesEntry.CONTENT_URI, null);
                    getSupportLoaderManager()
                            .restartLoader(TASK_LOADER_ID, null, ViewNotesActivity.this);

                    UserDefaults.WidgetInfo.setNotesCount(context,
                            objects.size());

                    int index = 0;

                    for (ParseObject currentObject : objects) {
                        ContentValues values = new ContentValues();
                        values.put(NotesContract.NotesEntry.COLUMN_AUTHOR_ID,
                                currentObject.getString("author_id"));
                        values.put(NotesContract.NotesEntry.COLUMN_AUTHOR,
                                currentObject.getString("author"));
                        values.put(NotesContract.NotesEntry.COLUMN_TITLE,
                                currentObject.getString("title"));
                        values.put(NotesContract.NotesEntry.COLUMN_CONTENT,
                                currentObject.getString("content"));

                        getContentResolver()
                                .insert(NotesContract.NotesEntry.CONTENT_URI.buildUpon()
                                            .appendPath("" + index).build(), values);

                        getContentResolver()
                                .notifyChange(NotesContract.NotesEntry.CONTENT_URI, null);

                        index++;
                    }

                    contentResolverLastIndex = index;

                    getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null,
                            ViewNotesActivity.this);

                    adapter = new NotesAdapter();
                    adapter.setDataSource(context, objects);

                    notesRecyclerView.setAdapter(adapter);
                    notesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_NOTE_REQUEST) {
            if (resultCode == RESULT_OK) {
                getContentResolver().delete(NotesContract.NotesEntry.CONTENT_URI, null, null);
                getContentResolver().notifyChange(NotesContract.NotesEntry.CONTENT_URI, null);
                getSupportLoaderManager()
                        .restartLoader(TASK_LOADER_ID, null, ViewNotesActivity.this);
                setDataSource();
            }
        }
        else if (requestCode == EDIT_NOTE_REQUEST) {
            if (resultCode == RESULT_OK) {
                getContentResolver().delete(NotesContract.NotesEntry.CONTENT_URI, null, null);
                getContentResolver().notifyChange(NotesContract.NotesEntry.CONTENT_URI, null);
                getSupportLoaderManager()
                        .restartLoader(TASK_LOADER_ID, null, ViewNotesActivity.this);
                setDataSource();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void adapterItemClicked(int position, ParseObject selectedObject) {
        String noteId = selectedObject.getObjectId();

        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra(EditNoteActivity.NOTE_ID_KEY, noteId);
        startActivityForResult(intent, EDIT_NOTE_REQUEST);

        if (MundleApplication.isLoggingEnabled) {
            Timber.d("Viewing note..." + noteId);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor currentCursor = null;

            @Override
            protected void onStartLoading() {
                if (currentTask != null) {
                    currentTask.cancel(true);
                    currentTask = null;
                }

                if (currentCursor != null) {
                    deliverResult(currentCursor);
                }
                else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                return getContentResolver().query(NotesContract.NotesEntry.CONTENT_URI,
                        null, null, null, null);
            }

            @Override
            public void deliverResult(Cursor data) {
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<Note> notes = new ArrayList<>();

        if (data.moveToFirst()) {
            do {
                Note currentNote = new Note();
                currentNote.setAuthor(data.getString(data.getColumnIndex(NotesContract.NotesEntry.COLUMN_AUTHOR)));
                currentNote.setAuthorId(data.getString(data.getColumnIndex(NotesContract.NotesEntry.COLUMN_AUTHOR_ID)));
                currentNote.setTitle(data.getString(data.getColumnIndex(NotesContract.NotesEntry.COLUMN_TITLE)));
                currentNote.setContent(data.getString(data.getColumnIndex(NotesContract.NotesEntry.COLUMN_CONTENT)));

                notes.add(currentNote);
            }
            while (data.moveToNext());
        }

        if (adapter != null) {
            adapter.setLoadedNotesDataSource(notes);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}
