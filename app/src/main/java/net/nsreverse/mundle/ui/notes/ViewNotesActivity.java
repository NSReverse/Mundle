package net.nsreverse.mundle.ui.notes;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import net.nsreverse.mundle.ui.adapters.NotesAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ViewNotesActivity extends AppCompatActivity
                               implements NotesAdapter.Delegate {

    private static final int ADD_NOTE_REQUEST = 101;
    private static final int EDIT_NOTE_REQUEST = 102;

    @BindView(R.id.recycler_view_notes_list) RecyclerView notesRecyclerView;
    @BindView(R.id.fab_create_note) FloatingActionButton fabAddNote;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(ParseUser.getCurrentUser().getUsername() + "'s Notes");
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
                    UserDefaults.WidgetInfo.setNotesCount(context,
                            objects.size());

                    NotesAdapter adapter = new NotesAdapter();
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
                setDataSource();
            }
        }
        else if (requestCode == EDIT_NOTE_REQUEST) {
            if (resultCode == RESULT_OK) {
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
}
