package net.nsreverse.mundle.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class CreateClassActivity extends AppCompatActivity {

    @BindView(R.id.edit_text_class_id) EditText idEditText;
    @BindView(R.id.edit_text_class_name) EditText nameEditText;
    @BindView(R.id.button_submit) Button submitButton;

    private static final String KEY_CLASS_ID = "classroom_name";
    private static final String KEY_INSTRUCTOR_NAME = "instructor_name";
    private static final String KEY_INSTRUCTOR_ID = "instructor_id";
    private static final String KEY_CLASS_NAME = "classroom_short_desc";

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getString(R.string.activity_title_create_class));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;

        ButterKnife.bind(this);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String classId = idEditText.getText().toString();
                String className = nameEditText.getText().toString();

                if (!classId.isEmpty() && !className.isEmpty()) {
                    ParseObject newObject = new ParseObject("Classroom");
                    newObject.put(KEY_CLASS_ID, classId);
                    newObject.put(KEY_INSTRUCTOR_NAME, ParseUser.getCurrentUser().getUsername());
                    newObject.put(KEY_INSTRUCTOR_ID, ParseUser.getCurrentUser().getObjectId());
                    newObject.put(KEY_CLASS_NAME, className);

                    newObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(context,
                                        "Saved your new classroom!",
                                        Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            }
                            else {
                                Toast.makeText(context,
                                        "There was an issue saving your new class: " +
                                            e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                setResult(RESULT_CANCELED);
                                finish();
                            }
                        }
                    });
                }
            }
        });

        if (savedInstanceState != null) {
            String classId = savedInstanceState.getString(KEY_CLASS_ID);
            String className = savedInstanceState.getString(KEY_CLASS_NAME);

            if (classId != null && !classId.isEmpty()) {
                idEditText.setText(classId);
            }

            if (className != null && !className.isEmpty()) {
                nameEditText.setText(className);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_class, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_cancel || item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(KEY_CLASS_ID, idEditText.getText().toString());
        outState.putString(KEY_CLASS_NAME, nameEditText.getText().toString());
    }
}
