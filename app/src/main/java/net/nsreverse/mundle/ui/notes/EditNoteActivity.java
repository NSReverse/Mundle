package net.nsreverse.mundle.ui.notes;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import net.nsreverse.mundle.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditNoteActivity extends AppCompatActivity {

    public static final String NOTE_ID_KEY = "note_id";

    @BindView(R.id.edit_text_note_title) EditText titleEditText;
    @BindView(R.id.edit_text_note_content) EditText contentEditText;
    @BindView(R.id.button_save_note) Button saveNoteButton;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getString(R.string.activity_edit_note));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;

        ButterKnife.bind(this);

        String noteId = getIntent().getStringExtra(NOTE_ID_KEY);

        ParseQuery<ParseObject> query = new ParseQuery<>("PersonalNote");
        query.whereEqualTo("objectId", noteId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                setupComponents(objects.get(0));
            }
        });
    }

    private void setupComponents(final ParseObject currentObject) {
        titleEditText.setText(currentObject.getString("title"));
        contentEditText.setText(currentObject.getString("content"));

        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEditText.getText().toString();
                final String content = contentEditText.getText().toString();

                currentObject.put("title", title);
                currentObject.put("content", content);
                currentObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(context,
                                    context.getString(R.string.content_existing_note_saved),
                                    Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                        }
                        else {
                            Toast.makeText(context,
                                    context.getString(R.string.content_failed_save_existing_note) +
                                        " " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            setResult(RESULT_CANCELED);
                        }

                        finish();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
