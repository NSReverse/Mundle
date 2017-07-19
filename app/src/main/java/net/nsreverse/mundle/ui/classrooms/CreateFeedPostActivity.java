package net.nsreverse.mundle.ui.classrooms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import net.nsreverse.mundle.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateFeedPostActivity extends AppCompatActivity {

    public static final String KEY_CLASS_ID = "class_id";

    @BindView(R.id.edit_text_feed_title) EditText feedTitleEditText;
    @BindView(R.id.edit_text_feed_content) EditText feedContentEditText;
    @BindView(R.id.button_save_feed_post) Button postButton;

    private String classId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_feed_post);

        classId = getIntent().getStringExtra(KEY_CLASS_ID);

        ButterKnife.bind(this);

        setupComponents();
    }

    private void setupComponents() {
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = feedTitleEditText.getText().toString();
                String content = feedContentEditText.getText().toString();

                if (!title.isEmpty() && !content.isEmpty()) {
                    ParseObject newObject = new ParseObject("ClassroomFeed");
                    newObject.put("classroom_id", classId);
                    newObject.put("author", ParseUser.getCurrentUser().getUsername());
                    newObject.put("title", title);
                    newObject.put("contents", content);
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
            }
        });
    }
}
