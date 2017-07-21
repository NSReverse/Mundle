package net.nsreverse.mundle.ui.notes;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import net.nsreverse.mundle.R;
import net.nsreverse.mundle.data.notesprovider.NotesContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateNoteActivity extends AppCompatActivity {

    @BindView(R.id.edit_text_note_title) EditText titleEditText;
    @BindView(R.id.edit_text_note_content) EditText contentEditText;
    @BindView(R.id.button_save_note) Button saveNoteButton;

    public static final String KEY_NEXT_INDEX = "next_index";

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getString(R.string.activity_new_note));

        context = this;

        ButterKnife.bind(this);

        setupComponents();
    }

    private void setupComponents() {
        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title = titleEditText.getText().toString();
                final String content = contentEditText.getText().toString();

                if (!title.isEmpty() && !content.isEmpty()) {
                    ParseObject newObject = new ParseObject("PersonalNote");
                    newObject.put("author", ParseUser.getCurrentUser().getUsername());
                    newObject.put("content", content);
                    newObject.put("title", title);
                    newObject.put("author_id", ParseUser.getCurrentUser().getObjectId());
                    newObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(context,
                                        context.getString(R.string.content_new_note_saved),
                                        Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);

                                ContentValues values = new ContentValues();
                                values.put(NotesContract.NotesEntry.COLUMN_AUTHOR_ID,
                                        ParseUser.getCurrentUser().getObjectId());
                                values.put(NotesContract.NotesEntry.COLUMN_AUTHOR,
                                        ParseUser.getCurrentUser().getUsername());
                                values.put(NotesContract.NotesEntry.COLUMN_TITLE,
                                        title);
                                values.put(NotesContract.NotesEntry.COLUMN_CONTENT,
                                        content);

                                getContentResolver().insert(NotesContract.NotesEntry.CONTENT_URI
                                        .buildUpon()
                                        .appendPath("" + getIntent().getIntExtra(KEY_NEXT_INDEX, 0))
                                        .build(),
                                        values);
                            }
                            else {
                                Toast.makeText(context,
                                        context.getString(R.string.content_failed_save_new_note) +
                                                " " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                setResult(RESULT_CANCELED);
                            }

                            finish();
                        }
                    });
                }
            }
        });
    }
}
