package net.nsreverse.mundle.ui.notes;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateNoteActivity extends AppCompatActivity {

    @BindView(R.id.edit_text_note_title) EditText titleEditText;
    @BindView(R.id.edit_text_note_content) EditText contentEditText;
    @BindView(R.id.button_save_note) Button saveNoteButton;

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
                String title = titleEditText.getText().toString();
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
                                        "Your new note has been saved!",
                                        Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                            }
                            else {
                                Toast.makeText(context,
                                        "Failed to save your note: " + e.getMessage(),
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
