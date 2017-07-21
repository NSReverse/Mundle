package net.nsreverse.mundle.ui.classrooms;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import net.nsreverse.mundle.MundleApplication;
import net.nsreverse.mundle.R;
import net.nsreverse.mundle.data.notifier.FCMAsyncTask;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class CreateFeedPostActivity extends AppCompatActivity {

    public static final String KEY_CLASS_ID = "class_id";
    public static final String KEY_CLASS_NAME = "class_name";

    @BindView(R.id.edit_text_feed_title) EditText feedTitleEditText;
    @BindView(R.id.edit_text_feed_content) EditText feedContentEditText;
    @BindView(R.id.button_save_feed_post) Button postButton;

    private String classId;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_feed_post);

        context = this;

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

                                ParseQuery<ParseObject> query =
                                        new ParseQuery<>("ClassroomSubscriptions");
                                query.whereEqualTo("classroom_id",
                                        getIntent().getStringExtra(KEY_CLASS_ID));
                                query.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if (e == null && objects.size() > 0) {
                                            for (ParseObject currentSubscription : objects) {
                                                ParseQuery<ParseUser> usersQuery = ParseUser.getQuery();
                                                usersQuery.whereEqualTo("objectId",
                                                        currentSubscription.getString("subscriber_id"));

                                                try {
                                                    List<ParseUser> users = usersQuery.find();

                                                    String message = context.getString(R.string.notify_new_feed_post);

                                                    if (users.size() > 0) {
                                                        String fcmToken = users.get(0)
                                                                .getString("fcm_token");
                                                        new FCMAsyncTask(context).execute(message +
                                                                " " +
                                                                getIntent().getStringExtra(KEY_CLASS_NAME),
                                                                fcmToken);
                                                    }
                                                }
                                                catch (ParseException ex) {
                                                    if (MundleApplication.isLoggingEnabled) {
                                                        Timber.d("Unable to get user: " +
                                                                ex.getMessage());
                                                    }

                                                    finish();
                                                }
                                            }
                                        }
                                        else {
                                            if (e != null) {
                                                if (MundleApplication.isLoggingEnabled) {
                                                    Timber.d("Unable to retrieve subscriptions: " +
                                                            e.getMessage());
                                                }
                                            }
                                            else {
                                                if (MundleApplication.isLoggingEnabled) {
                                                    Timber.d("No subscriptions found.");
                                                }
                                            }

                                            finish();
                                        }
                                    }
                                });
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
