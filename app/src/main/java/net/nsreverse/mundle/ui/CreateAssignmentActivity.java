package net.nsreverse.mundle.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.nsreverse.mundle.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateAssignmentActivity extends AppCompatActivity {

    public static final String KEY_CLASS_ID = "class_id";
    public static final String KEY_CLASS_NAME = "class_name";

    @BindView(R.id.edit_text_assign_title) EditText titleEditText;
    @BindView(R.id.edit_text_assign_content) EditText contentEditText;
    @BindView(R.id.date_picker_due) DatePicker dueDatePicker;
    @BindView(R.id.button_save_assign_post) Button saveAssignButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_assignment);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getIntent().getStringExtra(KEY_CLASS_NAME));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        setupComponents();
    }

    private void setupComponents() {
        saveAssignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEditText.getText().toString();
                String content = contentEditText.getText().toString();

                int day = dueDatePicker.getDayOfMonth();
                int month = dueDatePicker.getMonth();
                int year = dueDatePicker.getYear();

                Calendar calendar = new GregorianCalendar(year, month, day);
                Date dueDate = calendar.getTime();

                ParseObject newObject = new ParseObject("Assignments");
                newObject.put("classroom_id", getIntent().getStringExtra(KEY_CLASS_ID));
                newObject.put("author", ParseUser.getCurrentUser().getUsername());
                newObject.put("title", title);
                newObject.put("contents", content);
                newObject.put("due_date", dueDate);

                newObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            setResult(RESULT_OK);
                        }
                        else {
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
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
